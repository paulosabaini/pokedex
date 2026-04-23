package org.sabaini.pokedex.domain.usecase

import org.sabaini.pokedex.domain.model.Pokemon
import org.sabaini.pokedex.domain.repository.PokemonRepository
import javax.inject.Inject

class GetPokemonListUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(page: Int, refresh: Boolean = false): List<Pokemon> {
        return repository.getPokemonList(page, refresh)
    }
}
