package org.sabaini.pokedex.domain.usecase

import org.sabaini.pokedex.domain.model.PokemonInfo
import org.sabaini.pokedex.domain.repository.PokemonRepository
import javax.inject.Inject

class GetPokemonInfoUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(name: String, refresh: Boolean = false): PokemonInfo? {
        return repository.getPokemonInfo(name, refresh)
    }
}
