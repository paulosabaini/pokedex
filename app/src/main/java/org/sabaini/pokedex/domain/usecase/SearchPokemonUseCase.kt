package org.sabaini.pokedex.domain.usecase

import org.sabaini.pokedex.domain.model.Pokemon
import org.sabaini.pokedex.domain.repository.PokemonRepository
import javax.inject.Inject

class SearchPokemonUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(query: String): List<Pokemon> {
        return repository.searchPokemon(query)
    }
}
