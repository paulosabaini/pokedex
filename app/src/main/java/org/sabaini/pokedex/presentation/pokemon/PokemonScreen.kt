package org.sabaini.pokedex.presentation.pokemon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import org.sabaini.pokedex.R
import org.sabaini.pokedex.presentation.pokemon.tabs.PokemonInfoTabs
import org.sabaini.pokedex.presentation.theme.LightGray
import org.sabaini.pokedex.presentation.theme.Red
import org.sabaini.pokedex.util.toTitleCase

@Composable
fun PokemonScreen(
    viewModel: PokemonViewModel,
    onDominantColor: (Color) -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState.value) {
        is PokemonScreenUiState.Loading -> {
            onDominantColor(Red)
            PokemonInfoLoading()
        }

        is PokemonScreenUiState.Error -> {
            onDominantColor(Red)
            PokemonInfoError()
        }

        is PokemonScreenUiState.Success -> PokemonInfo(
            pokemon = (uiState.value as PokemonScreenUiState.Success).pokemonInfo,
            onDominantColor = onDominantColor,
        )
    }
}

@Composable
private fun PokemonInfoLoading() {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth().padding(dimensionResource(R.dimen.dimen_of_16_dp)),
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(dimensionResource(R.dimen.dimen_of_48_dp))
                .padding(bottom = dimensionResource(R.dimen.dimen_of_10_dp)),
        )
    }
}

@Composable
private fun PokemonInfoError() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(dimensionResource(R.dimen.dimen_of_16_dp)),
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dimen_of_16_dp)))
        Icon(
            Icons.Filled.Error,
            contentDescription = stringResource(R.string.error),
            tint = Red,
            modifier = Modifier.size(dimensionResource(R.dimen.dimen_of_64_dp)),
        )
        Text(
            text = stringResource(R.string.loading_error),
            color = Color.White,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun PokemonInfo(
    pokemon: PokemonInfoUiState,
    onDominantColor: (Color) -> Unit,
) {
    LaunchedEffect(pokemon.backgroundColor) {
        onDominantColor(pokemon.backgroundColor ?: LightGray)
    }

    Column(modifier = Modifier.background(color = pokemon.backgroundColor ?: LightGray)) {
        Column(modifier = Modifier.padding(dimensionResource(R.dimen.dimen_of_16_dp))) {
            PokemonNameAndNumber(
                pokemonName = pokemon.name,
                pokemonNumber = pokemon.getFormattedPokemonNumber(),
            )
            PokemonTypes(types = pokemon.types)
            PokemonInfoImage(
                pokemonName = pokemon.name,
                pokemonImageUrl = pokemon.getImageUrl(),
            )
        }
        PokemonInfoTabs(pokemon)
    }
}

@Composable
private fun PokemonNameAndNumber(pokemonName: String, pokemonNumber: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.dimen_of_5_dp)),
    ) {
        Text(
            text = pokemonName.toTitleCase(),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = dimensionResource(R.dimen.dimen_of_35_sp).value.sp,
        )
        Text(
            text = pokemonNumber,
            color = Color.White,
            fontSize = dimensionResource(R.dimen.dimen_of_25_sp).value.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun PokemonTypes(types: List<String>) {
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.dimen_of_5_dp)),
    ) {
        types.forEach {
            PokemonType(
                type = it,
                modifier = Modifier.padding(end = dimensionResource(R.dimen.dimen_of_5_dp)),
            )
        }
    }
}

@Composable
private fun PokemonInfoImage(
    pokemonName: String,
    pokemonImageUrl: String,
) {
    AsyncImage(
        model = pokemonImageUrl,
        contentDescription = pokemonName,
        modifier = Modifier
            .fillMaxWidth()
            .size(dimensionResource(R.dimen.dimen_of_150_dp))
            .padding(bottom = dimensionResource(R.dimen.dimen_of_10_dp)),
    )
}
