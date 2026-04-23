package org.sabaini.pokedex.presentation.pokedex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.sabaini.pokedex.data.PokemonsSource
import org.sabaini.pokedex.domain.usecase.UpdatePokemonColorUseCase
import org.sabaini.pokedex.util.Constants.PAGING_SIZE
import javax.inject.Inject

@HiltViewModel
class PokedexViewModel @Inject constructor(
    private val source: PokemonsSource,
    private val updatePokemonColorUseCase: UpdatePokemonColorUseCase,
) : ViewModel() {

    val pokeFlow: Flow<PagingData<PokemonUiState>> = Pager(PagingConfig(PAGING_SIZE)) {
        source
    }.flow
        .map { pagingData -> pagingData.map { it.toUiState() } }
        .cachedIn(viewModelScope)

    fun updatePokemonColor(pokemonName: String, color: Int) {
        viewModelScope.launch {
            updatePokemonColorUseCase(pokemonName, color)
        }
    }
}
