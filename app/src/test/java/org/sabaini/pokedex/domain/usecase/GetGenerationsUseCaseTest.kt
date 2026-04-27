package org.sabaini.pokedex.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.sabaini.pokedex.domain.model.Generation
import org.sabaini.pokedex.domain.repository.PokemonRepository

class GetGenerationsUseCaseTest {

    private val repository: PokemonRepository = mockk()
    private val useCase = GetGenerationsUseCase(repository)

    @Test
    fun `invoke should call repository getGenerations`() = runTest {
        // Given
        val expectedGenerations = listOf(Generation("gen1", "url1", "Generation I"))
        coEvery { repository.getGenerations() } returns expectedGenerations

        // When
        val result = useCase()

        // Then
        assertEquals(expectedGenerations, result)
        coVerify { repository.getGenerations() }
    }
}
