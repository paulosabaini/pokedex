package org.sabaini.pokedex.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import org.sabaini.pokedex.data.local.PokemonInfoEvolutionLocalModel
import org.sabaini.pokedex.data.local.PokemonInfoLocalModel
import org.sabaini.pokedex.data.local.PokemonLocalDataSource
import org.sabaini.pokedex.data.local.PokemonLocalModel
import org.sabaini.pokedex.data.mapper.toDomain
import org.sabaini.pokedex.data.remote.PokemonInfoApiModel
import org.sabaini.pokedex.data.remote.PokemonRemoteDataSource
import org.sabaini.pokedex.data.remote.asLocalModel
import org.sabaini.pokedex.data.remote.asStatLocalModel
import org.sabaini.pokedex.domain.model.Pokemon
import org.sabaini.pokedex.domain.model.PokemonEvolution
import org.sabaini.pokedex.domain.model.PokemonInfo
import org.sabaini.pokedex.domain.model.PokemonStat
import org.sabaini.pokedex.domain.repository.PokemonRepository
import org.sabaini.pokedex.util.Constants
import org.sabaini.pokedex.util.Constants.BLANK
import org.sabaini.pokedex.util.Constants.ENGLISH
import org.sabaini.pokedex.util.Constants.FIRE_RED
import org.sabaini.pokedex.util.Constants.MINUS_ONE
import org.sabaini.pokedex.util.Constants.NEW_LINE
import org.sabaini.pokedex.util.Constants.ONE
import org.sabaini.pokedex.util.Constants.SLASH
import org.sabaini.pokedex.util.Constants.SPACE
import org.sabaini.pokedex.util.Constants.ZERO
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val pokemonRemoteDataSource: PokemonRemoteDataSource,
    private val pokemonLocalDataSource: PokemonLocalDataSource,
    private val externalScope: CoroutineScope,
) : PokemonRepository {

    override suspend fun getPokemonList(page: Int, refresh: Boolean): List<Pokemon> {
        return if (refresh) {
            externalScope.async {
                pokemonRemoteDataSource.fetchPokemonList(page = page).also { networkResult ->
                    pokemonLocalDataSource.insertPokemons(networkResult.results.asLocalModel(page = page))
                }
                pokemonLocalDataSource.fetchPokemons(page).toDomain()
            }.await()
        } else {
            pokemonLocalDataSource.fetchPokemons(page).toDomain()
        }
    }

    override suspend fun getPokemonInfo(
        name: String,
        refresh: Boolean,
    ): PokemonInfo? {
        return if (refresh) {
            externalScope.async {
                pokemonRemoteDataSource.fetchPokemonInfo(name).also { networkResult ->
                    val localPokemon = saveLocalPokemonInfo(networkResult)
                    insertPokemonInfoEvolutions(localPokemon)
                }
                getLocalPokemonInfo(name)
            }.await()
        } else {
            getLocalPokemonInfo(name)
        }
    }

    private suspend fun insertPokemonInfoEvolutions(localPokemon: PokemonInfoLocalModel) {
        val pokemonEvolutions =
            pokemonRemoteDataSource.fetchPokemonInfoEvolutions(localPokemon.evolutionChainId)

        val localModelEvolutions = mutableListOf<PokemonInfoEvolutionLocalModel>()
        var chain = pokemonEvolutions.chain
        var evolutionCounter = ZERO

        while (evolutionCounter != MINUS_ONE) {
            val minLevel = chain.evolutionDetails.firstOrNull()?.minLevel ?: ZERO
            val pokemonInfo =
                pokemonRemoteDataSource.fetchPokemonInfo(chain.species.name)
            saveLocalPokemonInfo(pokemonInfo)
            val evolution =
                PokemonInfoEvolutionLocalModel(
                    localPokemon.evolutionChainId.toInt(),
                    pokemonInfo.id,
                    minLevel,
                )
            evolutionCounter++
            localModelEvolutions.add(evolution)

            if (chain.evolvesTo.isNotEmpty()) {
                chain = chain.evolvesTo.first()
            } else {
                evolutionCounter = MINUS_ONE
            }
        }
        pokemonLocalDataSource.insertPokemonInfoEvolution(localModelEvolutions)
    }

    private suspend fun saveLocalPokemonInfo(pokeInfo: PokemonInfoApiModel): PokemonInfoLocalModel {
        val species = pokemonRemoteDataSource.fetchPokemonInfoSpecies(pokeInfo.id.toString())
        val descriptionText =
            species.flavorTextEntries.find { it.version.name == FIRE_RED && it.language.name == ENGLISH }?.flavorText?.replace(
                NEW_LINE,
                SPACE,
            ) ?: BLANK
        val evolutionChainId =
            species.evolutionChain.url.split(SLASH.toRegex()).dropLast(ONE).last()
        val localStats = pokeInfo.stats.asStatLocalModel(pokeInfo.id)
        pokemonLocalDataSource.insertPokemonInfoStat(localStats)
        val pokemon = pokeInfo.asLocalModel()
            .copy(description = descriptionText, evolutionChainId = evolutionChainId)
        pokemonLocalDataSource.insertPokemonInfo(pokemon)
        return pokemon
    }

    private suspend fun getLocalPokemonInfo(name: String): PokemonInfo? {
        val pokemonInfo = pokemonLocalDataSource.fetchPokemonInfoByName(name)
        return pokemonInfo?.toDomain()?.let { domainInfo ->
            val pokedexPokemon = pokemonLocalDataSource.fetchPokemon(name)
            domainInfo.copy(
                evolutionChain = getEvolutionChain(pokemonInfo.evolutionChainId.toInt()),
                baseStats = getBaseStats(pokemonInfo.id),
                backgroundColor = pokedexPokemon.backgroundColor,
            )
        }
    }

    private suspend fun getEvolutionChain(evolutionChainId: Int): List<PokemonEvolution> {
        val pokemonEvolutions =
            pokemonLocalDataSource.fetchPokemonInfoEvolution(evolutionChainId)
        return pokemonEvolutions.mapNotNull { evolutionLocal ->
            val pokemon = pokemonLocalDataSource.fetchPokemonInfo(evolutionLocal.idPokemon)
            pokemon.toDomain()?.let { domainPokemon ->
                PokemonEvolution(
                    pokemon = domainPokemon,
                    minLevel = evolutionLocal.minLevel,
                )
            }
        }
    }

    private suspend fun getBaseStats(id: Int): List<PokemonStat> {
        val pokemonStats = pokemonLocalDataSource.fetchPokemonInfoStat(id)
        return pokemonStats.map {
            PokemonStat(
                name = it.name,
                baseState = it.baseState,
            )
        }
    }

    override suspend fun updatePokemonBackgroundColor(name: String, color: Int?) {
        pokemonLocalDataSource.updatePokemonBackgroundColor(name, color)
    }

    override suspend fun searchPokemon(query: String): List<Pokemon> {
        val localResults = pokemonLocalDataSource.searchPokemons(query)
        return if (localResults.isNotEmpty()) {
            localResults.toDomain()
        } else {
            try {
                val networkResult = pokemonRemoteDataSource.fetchPokemonInfo(query.lowercase())
                val pokemonLocal = PokemonLocalModel(
                    name = networkResult.name,
                    url = "${Constants.BASE_URL}pokemon/${networkResult.id}/",
                    page = MINUS_ONE,
                )
                pokemonLocalDataSource.insertPokemons(listOf(pokemonLocal))
                listOf(pokemonLocal.toDomain())
            } catch (_: Exception) {
                emptyList()
            }
        }
    }
}
