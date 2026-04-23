package org.sabaini.pokedex.presentation.pokedex

import androidx.compose.ui.graphics.Color
import org.sabaini.pokedex.domain.model.Pokemon
import org.sabaini.pokedex.util.Constants.ONE
import org.sabaini.pokedex.util.Constants.SLASH
import org.sabaini.pokedex.util.PokemonUtils.getDisplayPokemonNumber
import org.sabaini.pokedex.util.PokemonUtils.getPokemonImageUrl

data class PokemonUiState(
    val page: Int,
    val name: String,
    val url: String,
    val backgroundColor: Color? = null,
) {
    private fun getPokemonNumber(): String {
        return url.split(SLASH.toRegex()).dropLast(ONE).last()
    }

    fun getFormattedPokemonNumber(): String {
        return getDisplayPokemonNumber(getPokemonNumber())
    }

    fun getImageUrl(): String {
        return getPokemonImageUrl(getPokemonNumber())
    }
}

fun Pokemon.toUiState(): PokemonUiState {
    return PokemonUiState(
        page = this.page,
        name = this.name,
        url = this.url,
        backgroundColor = this.backgroundColor?.let { Color(it) },
    )
}
