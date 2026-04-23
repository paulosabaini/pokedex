package org.sabaini.pokedex.presentation.pokemon

import androidx.compose.ui.graphics.Color
import org.sabaini.pokedex.domain.model.PokemonEvolution
import org.sabaini.pokedex.domain.model.PokemonInfo
import org.sabaini.pokedex.domain.model.PokemonStat
import org.sabaini.pokedex.util.Constants
import org.sabaini.pokedex.util.Enums
import org.sabaini.pokedex.util.PokemonUtils
import java.util.Locale

data class PokemonInfoUiState(
    val id: Int = Constants.ZERO,
    val name: String = Constants.BLANK,
    val types: List<String> = listOf(),
    val description: String = Constants.BLANK,
    val height: Int = Constants.ZERO,
    val weight: Int = Constants.ZERO,
    var backgroundColor: Color? = null,
    var borderColor: Color? = null,
    val baseStats: List<PokemonInfoStatUiState> = listOf(),
    val evolutionChain: List<PokemonInfoEvolutionUiState> = listOf(),
) {
    fun getFormattedPokemonNumber(): String {
        return PokemonUtils.getDisplayPokemonNumber(id.toString())
    }

    fun getImageUrl(): String {
        return PokemonUtils.getPokemonImageUrl(id.toString())
    }

    fun getFormattedHeight(): String = String.format(Locale.getDefault(),"%.1f m", height.toFloat() / Constants.TEN)

    fun getFormattedWeight(): String = String.format(Locale.getDefault(),"%.1f kg", weight.toFloat() / Constants.TEN)

    fun getBackgroundColor(): Color {
        return backgroundColor ?: Color.Transparent
    }

    fun getBorderColor(): Color {
        return borderColor ?: Color.Transparent
    }
}

data class PokemonInfoStatUiState(
    val name: String,
    val baseState: Float,
    val color: Color,
)

data class PokemonInfoEvolutionUiState(
    val pokemon: PokemonInfoUiState,
    val minLevel: Int,
)

fun PokemonInfo.toUiState(): PokemonInfoUiState {
    return PokemonInfoUiState(
        id = this.id,
        name = this.name,
        types = this.types,
        description = this.description,
        height = this.height,
        weight = this.weight,
        backgroundColor = this.backgroundColor?.let { Color(it) },
        baseStats = this.baseStats.map { it.toUiState() },
        evolutionChain = this.evolutionChain.map { it.toUiState() },
    )
}

fun PokemonStat.toUiState(): PokemonInfoStatUiState {
    val statEnum = try {
        Enums.StatType.valueOf(this.name.replace("-", "_").uppercase())
    } catch (_: IllegalArgumentException) {
        Enums.StatType.HP // Default to HP or some other fallback
    }
    return PokemonInfoStatUiState(
        name = statEnum.stat,
        baseState = this.baseState / 100f,
        color = statEnum.color,
    )
}

fun PokemonEvolution.toUiState(): PokemonInfoEvolutionUiState {
    return PokemonInfoEvolutionUiState(
        pokemon = this.pokemon.toUiState(),
        minLevel = this.minLevel,
    )
}
