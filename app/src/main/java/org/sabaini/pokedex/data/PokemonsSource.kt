package org.sabaini.pokedex.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.sabaini.pokedex.domain.model.Pokemon
import org.sabaini.pokedex.domain.usecase.GetPokemonListUseCase
import org.sabaini.pokedex.domain.usecase.SearchPokemonUseCase
import org.sabaini.pokedex.util.Constants.ONE
import org.sabaini.pokedex.util.Constants.ZERO
import javax.inject.Inject

class PokemonsSource(
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val searchPokemonUseCase: SearchPokemonUseCase,
    private val query: String? = null,
) :
    PagingSource<Int, Pokemon>() {

    class Factory @Inject constructor(
        private val getPokemonListUseCase: GetPokemonListUseCase,
        private val searchPokemonUseCase: SearchPokemonUseCase,
    ) {
        fun create(query: String? = null): PokemonsSource {
            return PokemonsSource(getPokemonListUseCase, searchPokemonUseCase, query)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        return try {
            if (!query.isNullOrBlank()) {
                val pokemons = searchPokemonUseCase(query)
                LoadResult.Page(
                    data = pokemons,
                    prevKey = null,
                    nextKey = null,
                )
            } else {
                val nextPage = params.key ?: ZERO

                var pokemons = getPokemonListUseCase(nextPage, false)
                if (pokemons.isEmpty()) {
                    pokemons = getPokemonListUseCase(nextPage, true)
                }

                LoadResult.Page(
                    data = pokemons,
                    prevKey = if (nextPage == ZERO) null else nextPage - ONE,
                    nextKey = nextPage.plus(ONE),
                )
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int? {
        return state.anchorPosition
    }
}
