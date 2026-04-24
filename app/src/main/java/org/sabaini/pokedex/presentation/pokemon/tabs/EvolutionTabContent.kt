package org.sabaini.pokedex.presentation.pokemon.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import org.sabaini.pokedex.R
import org.sabaini.pokedex.presentation.pokemon.PokemonType
import org.sabaini.pokedex.presentation.pokemon.PokemonInfoUiState
import org.sabaini.pokedex.util.Constants
import org.sabaini.pokedex.util.toTitleCase

@Composable
fun EvolutionContent(pokemon: PokemonInfoUiState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(dimensionResource(R.dimen.dimen_of_15_dp)),
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
        ) {
            items(pokemon.evolutionChain) { evolution ->
                PokemonEvolution(
                    pokemon = evolution.pokemon,
                    minLevel = evolution.minLevel,
                )
            }
        }
    }
}

@Composable
private fun PokemonEvolution(
    pokemon: PokemonInfoUiState,
    minLevel: Int,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (minLevel != Constants.ZERO) EvolutionArrow(minLevel.toString())
        PokemonEvolutionDetails(pokemon)
    }
}

@Composable
private fun EvolutionArrow(minLevel: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            Icons.Filled.ArrowDownward,
            contentDescription = stringResource(R.string.arrow),
            tint = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = stringResource(R.string.level_value, minLevel),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = dimensionResource(R.dimen.dimen_of_12_sp).value.sp,
        )
    }
}

@Composable
private fun PokemonEvolutionDetails(pokemon: PokemonInfoUiState) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(dimensionResource(R.dimen.dimen_of_10_dp)),
    ) {
        PokemonEvolutionImage(pokemon.name, pokemon.getImageUrl())
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.dimen_of_16_dp)))
        PokemonEvolutionNameAndTypes(pokemon)
    }
}

@Composable
private fun PokemonEvolutionImage(
    pokemonName: String,
    pokemonImageUrl: String,
) {
    AsyncImage(
        model = pokemonImageUrl,
        contentDescription = pokemonName,
        modifier = Modifier
            .size(dimensionResource(R.dimen.dimen_of_80_dp)),
    )
}

@Composable
private fun PokemonEvolutionNameAndTypes(pokemon: PokemonInfoUiState) {
    Column {
        Text(text = pokemon.getFormattedPokemonNumber(), color = MaterialTheme.colorScheme.onSurface)
        Text(
            text = pokemon.name.toTitleCase(),
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dimen_of_4_dp)))
        Row {
            pokemon.types.forEach { type ->
                PokemonType(
                    type = type,
                    modifier = Modifier.padding(dimensionResource(R.dimen.dimen_of_1_dp)),
                )
            }
        }
    }
}
