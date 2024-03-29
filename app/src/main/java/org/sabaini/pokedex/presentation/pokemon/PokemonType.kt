package org.sabaini.pokedex.presentation.pokemon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.sp
import org.sabaini.pokedex.R
import org.sabaini.pokedex.util.Enums

@Composable
fun PokemonType(
    modifier: Modifier = Modifier,
    type: String,
) {
    Box(
        modifier = modifier
            .background(
                Enums.PokemonTypeColor.valueOf(type.uppercase()).color,
                RoundedCornerShape(dimensionResource(R.dimen.dimen_of_10_dp)),
            )
            .padding(dimensionResource(R.dimen.dimen_of_5_dp)),
    ) {
        Text(
            text = type,
            color = Color.White,
            fontSize = dimensionResource(R.dimen.dimen_of_12_sp).value.sp,
        )
    }
}
