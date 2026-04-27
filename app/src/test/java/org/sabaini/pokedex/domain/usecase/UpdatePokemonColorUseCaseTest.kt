package org.sabaini.pokedex.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.sabaini.pokedex.domain.repository.PokemonRepository

class UpdatePokemonColorUseCaseTest {

    private val repository: PokemonRepository = mockk()
    private val useCase = UpdatePokemonColorUseCase(repository)

    @Test
    fun `invoke should call repository updatePokemonBackgroundColor`() = runTest {
        // Given
        val name = "bulbasaur"
        val color = 12345
        coEvery { repository.updatePokemonBackgroundColor(name, color) } returns Unit

        // When
        useCase(name, color)

        // Then
        coVerify { repository.updatePokemonBackgroundColor(name, color) }
    }
}
