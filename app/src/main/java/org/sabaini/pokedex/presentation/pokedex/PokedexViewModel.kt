package org.sabaini.pokedex.presentation.pokedex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.sabaini.pokedex.data.PokemonRepository
import org.sabaini.pokedex.data.PokemonsSource
import org.sabaini.pokedex.util.Constants.PAGING_SIZE
import javax.inject.Inject

@HiltViewModel
class PokedexViewModel @Inject constructor(
    private val source: PokemonsSource,
    private val repository: PokemonRepository,
) : ViewModel() {

    val pokeFlow: Flow<PagingData<PokemonUiState>> = Pager(PagingConfig(PAGING_SIZE)) {
        source
    }.flow.cachedIn(viewModelScope)

    fun updatePokemonColor(pokemonName: String, color: Int) {
        viewModelScope.launch {
            repository.updatePokemonBackgroundColor(pokemonName, color)
        }
    }
}
