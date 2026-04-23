package org.sabaini.pokedex.presentation.pokedex

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import org.sabaini.pokedex.R
import org.sabaini.pokedex.presentation.theme.PokedexTheme

@Composable
fun LoadingView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun LoadingItem() {
    CircularProgressIndicator(
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.dimen_of_16_dp))
            .wrapContentWidth(Alignment.CenterHorizontally)
            .size(dimensionResource(R.dimen.dimen_of_48_dp)),
    )
}

@Composable
fun ErrorItem(
    message: String,
    modifier: Modifier = Modifier,
    onClickRetry: () -> Unit,
) {
    Column(
        modifier = modifier.padding(dimensionResource(R.dimen.dimen_of_10_dp)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = message,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(dimensionResource(R.dimen.dimen_of_10_dp)),
        )
        Button(
            onClick = onClickRetry,
            contentPadding = PaddingValues(dimensionResource(R.dimen.dimen_of_10_dp)),
        ) {
            Text(text = stringResource(R.string.retry))
        }
    }
}

@Preview
@Composable
private fun LoadingViewPreview() {
    PokedexTheme {
        LoadingView()
    }
}

@Preview
@Composable
private fun LoadingItemPreview() {
    PokedexTheme {
        LoadingItem()
    }
}

@Preview
@Composable
private fun ErrorItemPreview() {
    PokedexTheme {
        ErrorItem(
            message = "Error message",
            onClickRetry = {},
        )
    }
}
