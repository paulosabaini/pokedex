package org.sabaini.pokedex.domain.usecase

import org.sabaini.pokedex.domain.model.Generation
import org.sabaini.pokedex.domain.repository.PokemonRepository
import javax.inject.Inject

class GetGenerationsUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(): List<Generation> {
        return repository.getGenerations()
    }
}
