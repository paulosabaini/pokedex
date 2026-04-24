package org.sabaini.pokedex.presentation.pokemon.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import org.sabaini.pokedex.R
import org.sabaini.pokedex.presentation.pokemon.PokemonInfoUiState

@Composable
fun AboutContent(pokemon: PokemonInfoUiState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(dimensionResource(R.dimen.dimen_of_15_dp)),
    ) {
        Text(
            text = pokemon.description,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Justify,
            fontSize = dimensionResource(R.dimen.dimen_of_16_sp).value.sp,
        )

        HeightAndWeightCard(
            height = pokemon.getFormattedHeight(),
            weight = pokemon.getFormattedWeight(),
        )
    }
}

@Composable
private fun HeightAndWeightCard(height: String, weight: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.dimen_of_3_dp)),
        modifier = Modifier
            .padding(
                vertical = dimensionResource(R.dimen.dimen_of_10_dp),
                horizontal = dimensionResource(R.dimen.dimen_of_5_dp),
            )
            .fillMaxWidth(),
    ) {
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            HeightAndWeightText(stringResource(R.string.height), height)
            HeightAndWeightText(stringResource(R.string.weight), weight)
        }
    }
}

@Composable
private fun HeightAndWeightText(title: String, value: String) {
    Column(
        modifier = Modifier.padding(dimensionResource(R.dimen.dimen_of_10_dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = title, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f))
        Text(text = value, color = MaterialTheme.colorScheme.onPrimaryContainer)
    }
}

@Preview
@Composable
private fun AboutContentPreview() {
    AboutContent(PokemonInfoUiState())
}
