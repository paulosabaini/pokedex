package org.sabaini.pokedex.presentation.pokedex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.sabaini.pokedex.data.PokemonsSource
import org.sabaini.pokedex.domain.model.Generation
import org.sabaini.pokedex.domain.usecase.GetGenerationsUseCase
import org.sabaini.pokedex.domain.usecase.UpdatePokemonColorUseCase
import org.sabaini.pokedex.util.Constants.PAGING_SIZE
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class PokedexViewModel @Inject constructor(
    private val pokemonsSourceFactory: PokemonsSource.Factory,
    private val getGenerationsUseCase: GetGenerationsUseCase,
    private val updatePokemonColorUseCase: UpdatePokemonColorUseCase,
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedGeneration = MutableStateFlow<String?>(null)
    val selectedGeneration: StateFlow<String?> = _selectedGeneration.asStateFlow()

    private val _generations = MutableStateFlow<List<Generation>>(emptyList())
    val generations: StateFlow<List<Generation>> = _generations.asStateFlow()

    init {
        fetchGenerations()
    }

    private fun fetchGenerations() {
        viewModelScope.launch {
            _generations.value = getGenerationsUseCase()
        }
    }

    val pokeFlow: Flow<PagingData<PokemonUiState>> = combine(
        searchQuery.debounce(300L),
        selectedGeneration
    ) { query, generation ->
        query to generation
    }.flatMapLatest { (query, generation) ->
        Pager(PagingConfig(PAGING_SIZE)) {
            pokemonsSourceFactory.create(query, generation)
        }.flow
    }.map { pagingData -> pagingData.map { it.toUiState() } }
        .cachedIn(viewModelScope)

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onGenerationSelected(generationName: String?) {
        _selectedGeneration.value = generationName
    }

    fun updatePokemonColor(pokemonName: String, color: Int) {
        viewModelScope.launch {
            updatePokemonColorUseCase(pokemonName, color)
        }
    }
}
