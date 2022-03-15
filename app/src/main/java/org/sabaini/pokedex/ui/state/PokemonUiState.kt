package org.sabaini.pokedex.ui.state

import org.sabaini.pokedex.util.Constants.BLANK

data class PokemonUiState(
    var page: Int,
    val name: String,
    val url: String
) {
    private fun getPokemonNumber(): String {
        return url.split("/".toRegex()).dropLast(1).last()
    }

    fun getFormatedPokemonNumber(): String {
        return "#" + getPokemonNumber().padStart(3, '0')
    }

    fun getImageUrl(): String {
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${getPokemonNumber()}.png"
    }
}

data class PokemonInfoUiState(
    val id: Int = 0,
    val name: String = BLANK,
    val types: List<String> = listOf(),
    val height: Int = 0,
    val weight: Int = 0
) {
    fun getFormatedPokemonNumber(): String {
        return "#" + id.toString().padStart(3, '0')
    }

    fun getImageUrl(): String {
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${id}.png"
    }
}