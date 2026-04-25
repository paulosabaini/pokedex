package org.sabaini.pokedex.domain.repository

import org.sabaini.pokedex.domain.model.Generation
import org.sabaini.pokedex.domain.model.Pokemon
import org.sabaini.pokedex.domain.model.PokemonInfo

interface PokemonRepository {
    suspend fun getPokemonList(page: Int, refresh: Boolean = false): List<Pokemon>
    suspend fun getPokemonInfo(name: String, refresh: Boolean = false): PokemonInfo?
    suspend fun updatePokemonBackgroundColor(name: String, color: Int?)
    suspend fun searchPokemon(query: String): List<Pokemon>
    suspend fun getGenerations(): List<Generation>
    suspend fun getPokemonByGeneration(generationName: String): List<Pokemon>
}
