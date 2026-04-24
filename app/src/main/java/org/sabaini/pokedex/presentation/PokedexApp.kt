package org.sabaini.pokedex.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.sabaini.pokedex.R
import org.sabaini.pokedex.presentation.Destinations.POKEDEX_SCREEN
import org.sabaini.pokedex.presentation.Destinations.POKEMON_SCREEN
import org.sabaini.pokedex.presentation.Destinations.POKEMON_SCREEN_ARGUMENT
import org.sabaini.pokedex.presentation.pokedex.PokedexScreen
import org.sabaini.pokedex.presentation.pokemon.PokemonScreen
import org.sabaini.pokedex.presentation.theme.PokedexTheme
import org.sabaini.pokedex.util.getContentColor

@Composable
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
fun PokedexApp() {
    PokedexTheme {
        val navController = rememberNavController()
        val upAvailable = remember { mutableStateOf(false) }
        var customTopAppBarColor by remember { mutableStateOf(Color.Transparent) }

        Scaffold(
            topBar = {
                PokedexTopBar(upAvailable, customTopAppBarColor) {
                    navController.navigateUp()
                }
            },
        ) { paddingValues ->
            NavHost(
                modifier = Modifier.padding(paddingValues),
                navController = navController,
                startDestination = POKEDEX_SCREEN,
            ) {
                composable(POKEDEX_SCREEN) {
                    upAvailable.value = false
                    PokedexScreen(viewModel = hiltViewModel(), onClickPokemon = { name ->
                        navController.navigate(
                            "$POKEMON_SCREEN/$name",
                        )
                    })
                }
                composable(
                    route = "$POKEMON_SCREEN/{$POKEMON_SCREEN_ARGUMENT}",
                    arguments = listOf(
                        navArgument(POKEMON_SCREEN_ARGUMENT) {
                            type = NavType.StringType
                        },
                    ),
                ) {
                    upAvailable.value = true
                    PokemonScreen(hiltViewModel()) {
                        customTopAppBarColor = it
                    }
                }
            }
        }
    }
}

@Composable
fun PokedexTopBar(
    upAvailable: MutableState<Boolean>,
    customTopAppBarColor: Color,
    onNavigateUp: () -> Unit,
) {
    if (upAvailable.value) {
        val contentColor = customTopAppBarColor.getContentColor()
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.pokedex),
                    fontWeight = FontWeight.Bold,
                    color = contentColor,
                )
            },
            navigationIcon = {
                IconButton(onClick = { onNavigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        tint = contentColor,
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = customTopAppBarColor,
            ),
        )
    } else {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.pokedex),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(dimensionResource(R.dimen.dimen_of_5_dp)),
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
        )
    }
}

object Destinations {
    const val POKEDEX_SCREEN = "pokedex"
    const val POKEMON_SCREEN = "pokemon"
    const val POKEMON_SCREEN_ARGUMENT = "pokemonName"
}
