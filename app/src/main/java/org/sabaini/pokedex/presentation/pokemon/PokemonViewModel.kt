package org.sabaini.pokedex.presentation.pokemon

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.sabaini.pokedex.domain.usecase.GetPokemonInfoUseCase
import org.sabaini.pokedex.presentation.Destinations
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPokemonInfoUseCase: GetPokemonInfoUseCase,
) : ViewModel() {

    private val pokemonName: String =
        checkNotNull(savedStateHandle[Destinations.POKEMON_SCREEN_ARGUMENT])

    private val _uiState = MutableStateFlow<PokemonScreenUiState>(PokemonScreenUiState.Loading)
    val uiState: StateFlow<PokemonScreenUiState> = _uiState

    init {
        fetchPokemonInfo(pokemonName)
    }

    private fun fetchPokemonInfo(name: String) {
        viewModelScope.launch {
            try {
                getPokemonInfoUseCase(name, false)?.let { pokemonInfo ->
                    _uiState.value = PokemonScreenUiState.Success(pokemonInfo.toUiState())
                } ?: getPokemonInfoUseCase(name, true)?.let { pokemonInfo ->
                    _uiState.value = PokemonScreenUiState.Success(pokemonInfo.toUiState())
                }
            } catch (e: Throwable) {
                _uiState.value = PokemonScreenUiState.Error(e)
            }
        }
    }
}
