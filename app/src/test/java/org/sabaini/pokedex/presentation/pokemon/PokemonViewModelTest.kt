package org.sabaini.pokedex.presentation.pokemon

import androidx.lifecycle.SavedStateHandle
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.sabaini.pokedex.domain.model.PokemonEvolution
import org.sabaini.pokedex.domain.model.PokemonInfo
import org.sabaini.pokedex.domain.model.PokemonStat
import org.sabaini.pokedex.domain.usecase.GetPokemonInfoUseCase
import org.sabaini.pokedex.presentation.Destinations
import org.sabaini.pokedex.util.MainDispatcherExtension

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonViewModelTest {

    @RegisterExtension
    @JvmField
    val mainDispatcherExtension = MainDispatcherExtension()

    private val getPokemonInfoUseCase: GetPokemonInfoUseCase = mockk()
    private val pokemonName = "bulbasaur"
    private val savedStateHandle = SavedStateHandle(mapOf(Destinations.POKEMON_SCREEN_ARGUMENT to pokemonName))

    @Test
    fun `init should fetch pokemon info and update state to Success`() = runTest {
        // Given
        val pokemonInfo = mockk<PokemonInfo> {
            every { name } returns pokemonName
            every { id } returns 1
            every { types } returns listOf("grass")
            every { description } returns ""
            every { height } returns 7
            every { weight } returns 69
            every { backgroundColor } returns null
            every { baseStats } returns emptyList<PokemonStat>()
            every { evolutionChain } returns emptyList<PokemonEvolution>()
        }
        coEvery { getPokemonInfoUseCase(pokemonName, false) } returns pokemonInfo

        // When
        val viewModel = PokemonViewModel(savedStateHandle, getPokemonInfoUseCase)

        // Then
        assertTrue(viewModel.uiState.value is PokemonScreenUiState.Success)
        val successState = viewModel.uiState.value as PokemonScreenUiState.Success
        assertTrue(successState.pokemonInfo.name == pokemonName)
    }

    @Test
    fun `init should update state to Error when fetch fails`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        coEvery { getPokemonInfoUseCase(pokemonName, any()) } throws exception

        // When
        val viewModel = PokemonViewModel(savedStateHandle, getPokemonInfoUseCase)

        // Then
        assertTrue(viewModel.uiState.value is PokemonScreenUiState.Error)
        val errorState = viewModel.uiState.value as PokemonScreenUiState.Error
        assertTrue(errorState.exception == exception)
    }
}
