package org.sabaini.pokedex.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.sabaini.pokedex.domain.model.PokemonInfo
import org.sabaini.pokedex.domain.repository.PokemonRepository

class GetPokemonInfoUseCaseTest {

    private val repository: PokemonRepository = mockk()
    private val useCase = GetPokemonInfoUseCase(repository)

    @Test
    fun `invoke should call repository getPokemonInfo`() = runTest {
        // Given
        val name = "bulbasaur"
        val refresh = false
        val expectedInfo = mockk<PokemonInfo>()
        coEvery { repository.getPokemonInfo(name, refresh) } returns expectedInfo

        // When
        val result = useCase(name, refresh)

        // Then
        assertEquals(expectedInfo, result)
        coVerify { repository.getPokemonInfo(name, refresh) }
    }
}
