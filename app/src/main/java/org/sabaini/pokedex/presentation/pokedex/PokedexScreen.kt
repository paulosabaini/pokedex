package org.sabaini.pokedex.presentation.pokedex

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.paging.compose.collectAsLazyPagingItems

@OptIn(ExperimentalLayoutApi::class)
@Composable
@ExperimentalComposeUiApi
fun PokedexScreen(viewModel: PokedexViewModel, onClickPokemon: (String) -> Unit) {
    val pokemons = viewModel.pokeFlow.collectAsLazyPagingItems()

    Scaffold { contentPadding ->
        PokemonList(
            modifier = Modifier.consumeWindowInsets(contentPadding).padding(contentPadding),
            pokemons = pokemons,
            onBackgroundColorChange = { pokemon, color ->
                viewModel.updatePokemonColor(pokemon, color.toArgb())
            },
            onClickPokemon = onClickPokemon,
        )
    }
}
