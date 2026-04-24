package org.sabaini.pokedex.presentation.pokedex

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import org.sabaini.pokedex.R
import org.sabaini.pokedex.presentation.theme.LightGray
import org.sabaini.pokedex.util.ColorUtils
import org.sabaini.pokedex.util.getContentColor
import org.sabaini.pokedex.util.toTitleCase

@Composable
fun PokemonCard(
    modifier: Modifier = Modifier,
    pokemon: PokemonUiState,
    onCalculateDominantColor: (Color) -> Unit,
    onItemClicked: (String) -> Unit,
) {
    var containerColor by remember(pokemon.name) {
        mutableStateOf(pokemon.backgroundColor ?: LightGray)
    }

    Card(
        onClick = { onItemClicked(pokemon.name) },
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = modifier
            .padding(dimensionResource(R.dimen.dimen_of_5_dp))
            .size(dimensionResource(R.dimen.dimen_of_150_dp)),
    ) {
        PokemonCardHeader(pokemon, containerColor)
        PokemonCardImage(
            pokemonName = pokemon.name,
            pokemonImageUrl = pokemon.getImageUrl(),
            backgroundColor = pokemon.backgroundColor,
            onCalculateDominantColor = { color ->
                containerColor = color
                onCalculateDominantColor(color)
            },
        )
    }
}

@Composable
private fun PokemonCardHeader(pokemon: PokemonUiState, containerColor: Color) {
    val contentColor = containerColor.getContentColor()
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.dimen_of_5_dp)),
    ) {
        Text(
            text = pokemon.name.toTitleCase(),
            color = contentColor,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = pokemon.getFormattedPokemonNumber(),
            color = contentColor,
        )
    }
}

@Composable
private fun PokemonCardImage(
    pokemonName: String,
    pokemonImageUrl: String,
    backgroundColor: Color?,
    onCalculateDominantColor: (Color) -> Unit,
) {
    var showLoading by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = dimensionResource(R.dimen.dimen_of_30_dp)),
        contentAlignment = Alignment.Center,
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(pokemonImageUrl)
                .crossfade(true)
                .build(),
            contentDescription = pokemonName,
            modifier = Modifier.fillMaxSize(),
            onState = { painterState ->
                showLoading = when (painterState) {
                    is AsyncImagePainter.State.Loading -> true
                    is AsyncImagePainter.State.Empty -> true
                    is AsyncImagePainter.State.Error -> true
                    is AsyncImagePainter.State.Success -> {
                        if (backgroundColor == null) {
                            ColorUtils.calculateDominantColor(painterState.result.drawable) {
                                onCalculateDominantColor(it)
                            }
                        }
                        false
                    }
                }
            },
        )

        if (showLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(dimensionResource(R.dimen.dimen_of_48_dp)),
            )
        }
    }
}
