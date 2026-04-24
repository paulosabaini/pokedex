package org.sabaini.pokedex.presentation.pokemon.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.sabaini.pokedex.R
import org.sabaini.pokedex.presentation.pokemon.PokemonInfoUiState
import org.sabaini.pokedex.util.Constants

@Composable
fun PokemonInfoTabs(pokemon: PokemonInfoUiState) {
    val tabs = listOf(TabItem.About, TabItem.BaseStats, TabItem.Evolution)
    val pagerState = rememberPagerState(
        initialPage = Constants.ZERO,
        initialPageOffsetFraction = 0f,
    ) {
        tabs.size
    }

    Column(
        modifier = Modifier.clip(
            RoundedCornerShape(
                topStart = dimensionResource(R.dimen.dimen_of_30_dp),
                topEnd = dimensionResource(R.dimen.dimen_of_30_dp),
            ),
        ),
    ) {
        TabsOptions(tabs = tabs, pagerState = pagerState)
        TabsContent(tabs = tabs, pagerState = pagerState, pokemon = pokemon)
    }
}

@Composable
private fun TabsOptions(tabs: List<TabItem>, pagerState: PagerState) {
    val scope = rememberCoroutineScope()

    PrimaryTabRow(
        selectedTabIndex = pagerState.currentPage,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        indicator = {
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(selectedTabIndex = pagerState.currentPage),
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
    ) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                text = {
                    Text(
                        text = tab.title,
                        fontSize = dimensionResource(R.dimen.dimen_of_13_sp).value.sp,
                    )
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
            )
        }
    }
}

@Composable
private fun TabsContent(tabs: List<TabItem>, pagerState: PagerState, pokemon: PokemonInfoUiState) {
    HorizontalPager(state = pagerState) { page ->
        tabs[page].screen(pokemon)
    }
}
