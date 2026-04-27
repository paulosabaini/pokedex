package org.sabaini.pokedex.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.sabaini.pokedex.domain.model.Pokemon
import org.sabaini.pokedex.domain.repository.PokemonRepository

class SearchPokemonUseCaseTest {

    private val repository: PokemonRepository = mockk()
    private val useCase = SearchPokemonUseCase(repository)

    @Test
    fun `invoke should call repository searchPokemon`() = runTest {
        // Given
        val query = "bulba"
        val expectedList = listOf(Pokemon(0, "bulbasaur", "url"))
        coEvery { repository.searchPokemon(query) } returns expectedList

        // When
        val result = useCase(query)

        // Then
        assertEquals(expectedList, result)
        coVerify { repository.searchPokemon(query) }
    }
}
