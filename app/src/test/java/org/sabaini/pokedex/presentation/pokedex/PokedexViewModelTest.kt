package org.sabaini.pokedex.presentation.pokedex

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.sabaini.pokedex.data.PokemonsSource
import org.sabaini.pokedex.domain.model.Generation
import org.sabaini.pokedex.domain.usecase.GetGenerationsUseCase
import org.sabaini.pokedex.domain.usecase.UpdatePokemonColorUseCase
import org.sabaini.pokedex.util.MainDispatcherExtension

@OptIn(ExperimentalCoroutinesApi::class)
class PokedexViewModelTest {

    @RegisterExtension
    @JvmField
    val mainDispatcherExtension = MainDispatcherExtension()

    private val pokemonsSourceFactory: PokemonsSource.Factory = mockk(relaxed = true)
    private val getGenerationsUseCase: GetGenerationsUseCase = mockk()
    private val updatePokemonColorUseCase: UpdatePokemonColorUseCase = mockk()

    @Test
    fun `init should fetch generations`() = runTest {
        // Given
        val generations = listOf(Generation("gen1", "url1", "Gen 1"))
        coEvery { getGenerationsUseCase() } returns generations

        // When
        val viewModel = PokedexViewModel(pokemonsSourceFactory, getGenerationsUseCase, updatePokemonColorUseCase)

        // Then
        assertEquals(generations, viewModel.generations.value)
    }

    @Test
    fun `onSearchQueryChange should update searchQuery state`() = runTest {
        // Given
        coEvery { getGenerationsUseCase() } returns emptyList()
        val viewModel = PokedexViewModel(pokemonsSourceFactory, getGenerationsUseCase, updatePokemonColorUseCase)
        val newQuery = "pikachu"

        // When
        viewModel.onSearchQueryChange(newQuery)

        // Then
        assertEquals(newQuery, viewModel.searchQuery.value)
    }

    @Test
    fun `onGenerationSelected should update selectedGeneration state`() = runTest {
        // Given
        coEvery { getGenerationsUseCase() } returns emptyList()
        val viewModel = PokedexViewModel(pokemonsSourceFactory, getGenerationsUseCase, updatePokemonColorUseCase)
        val generation = "generation-i"

        // When
        viewModel.onGenerationSelected(generation)

        // Then
        assertEquals(generation, viewModel.selectedGeneration.value)

        // And when deselected
        viewModel.onGenerationSelected(null)
        assertNull(viewModel.selectedGeneration.value)
    }

    @Test
    fun `updatePokemonColor should call updatePokemonColorUseCase`() = runTest {
        // Given
        coEvery { getGenerationsUseCase() } returns emptyList()
        coEvery { updatePokemonColorUseCase(any(), any()) } returns Unit
        val viewModel = PokedexViewModel(pokemonsSourceFactory, getGenerationsUseCase, updatePokemonColorUseCase)
        val pokemonName = "pikachu"
        val color = 0xFF0000

        // When
        viewModel.updatePokemonColor(pokemonName, color)

        // Then
        coVerify { updatePokemonColorUseCase(pokemonName, color) }
    }
}
