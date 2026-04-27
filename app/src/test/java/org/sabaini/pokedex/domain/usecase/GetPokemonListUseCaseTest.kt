package org.sabaini.pokedex.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.sabaini.pokedex.domain.model.Pokemon
import org.sabaini.pokedex.domain.repository.PokemonRepository

class GetPokemonListUseCaseTest {

    private val repository: PokemonRepository = mockk()
    private val useCase = GetPokemonListUseCase(repository)

    @Test
    fun `invoke should call repository getPokemonList`() = runTest {
        // Given
        val page = 0
        val refresh = false
        val expectedList = listOf(
            Pokemon(0, "bulbasaur", "url1"),
            Pokemon(0, "ivysaur", "url2")
        )
        coEvery { repository.getPokemonList(page, refresh) } returns expectedList

        // When
        val result = useCase(page, refresh)

        // Then
        assertEquals(expectedList, result)
        coVerify { repository.getPokemonList(page, refresh) }
    }
}
