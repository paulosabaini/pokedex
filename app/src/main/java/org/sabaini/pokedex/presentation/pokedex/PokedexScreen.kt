package org.sabaini.pokedex.presentation.pokedex

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import org.sabaini.pokedex.R
import org.sabaini.pokedex.util.Constants.BLANK

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
@ExperimentalComposeUiApi
fun PokedexScreen(viewModel: PokedexViewModel, onClickPokemon: (String) -> Unit) {
    val pokemons = viewModel.pokeFlow.collectAsLazyPagingItems()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedGeneration by viewModel.selectedGeneration.collectAsState()
    val generations by viewModel.generations.collectAsState()

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        TextField(
                            value = searchQuery,
                            onValueChange = { viewModel.onSearchQueryChange(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = dimensionResource(R.dimen.dimen_of_16_dp)),
                            placeholder = { Text(stringResource(R.string.search_pokemon)) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.onSearchQueryChange(BLANK) }) {
                                        Icon(Icons.Default.Close, contentDescription = null)
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(dimensionResource(R.dimen.dimen_of_16_dp)),
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                            ),
                        )
                    },
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.dimen_of_16_dp)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(generations) { generation ->
                        val isSelected = selectedGeneration == generation.name
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                viewModel.onGenerationSelected(
                                    if (isSelected) null else generation.name
                                )
                            },
                            label = { Text(generation.displayName) },
                            shape = RoundedCornerShape(dimensionResource(R.dimen.dimen_of_8_dp)),
                            modifier = Modifier.padding(end = dimensionResource(R.dimen.dimen_of_8_dp))
                        )
                    }
                }
            }
        },
    ) { contentPadding ->
        PokemonList(
            modifier = Modifier
                .consumeWindowInsets(contentPadding)
                .padding(contentPadding),
            pokemons = pokemons,
            onBackgroundColorChange = { pokemon, color ->
                viewModel.updatePokemonColor(pokemon, color.toArgb())
            },
            onClickPokemon = onClickPokemon,
        )
    }
}
