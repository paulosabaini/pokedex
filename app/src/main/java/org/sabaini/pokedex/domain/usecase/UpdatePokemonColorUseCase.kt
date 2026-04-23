package org.sabaini.pokedex.domain.usecase

import org.sabaini.pokedex.domain.repository.PokemonRepository
import javax.inject.Inject

class UpdatePokemonColorUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(name: String, color: Int?) {
        repository.updatePokemonBackgroundColor(name, color)
    }
}
