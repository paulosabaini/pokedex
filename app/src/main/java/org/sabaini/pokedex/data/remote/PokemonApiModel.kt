package org.sabaini.pokedex.data.remote

import com.google.gson.annotations.SerializedName
import org.sabaini.pokedex.data.local.PokemonLocalModel
import org.sabaini.pokedex.ui.state.PokemonInfoUiState

data class PokemonListApiModel(
    val count: Int,
    val next: String,
    val previous: String?,
    val results: List<PokemonApiModel>
)

data class PokemonApiModel(
    val name: String,
    val url: String
)

fun List<PokemonApiModel>.asLocalModel(page: Int): List<PokemonLocalModel> {
    return map {
        PokemonLocalModel(
            page = page,
            name = it.name,
            url = it.url
        )
    }
}

data class PokemonInfoApiModel(
    val id: Int,
    val name: String,
    val types: List<PokemonInfoTypesApiModel>,
    val height: Int,
    val weight: Int,
    // pokemon description and breading: https://pokeapi.co/api/v2/pokemon-species/1/
    // val stats: List<PokemonInfoStatsApiModel>,
    // evolution chain: https://pokeapi.co/api/v2/evolution-chain/1/
    // val moves: List<PokemonInfoMovesApiModel>
)

data class PokemonInfoTypesApiModel(
    val slot: Int,
    val type: PokemonInfoTypeApiModel
)

data class PokemonInfoTypeApiModel(
    val name: String,
    val url: String
)

data class PokemonInfoStatsApiModel(
    @SerializedName("base_stat")
    val baseState: Int,
    val effort: Int,
    val stat: PokemonInfoStatApiModel
)

data class PokemonInfoStatApiModel(
    val name: String,
    val url: String
)

data class PokemonInfoMovesApiModel(
    val move: PokemonInfoMoveApiModel,
    @SerializedName("version_group_details")
    val versionGroupDetails: PokemonInfoMoveDetailApiModel
)

data class PokemonInfoMoveApiModel(
    val name: String,
    val url: String
)

data class PokemonInfoMoveDetailApiModel(
    @SerializedName("level_learned_at")
    val levelLearnedAt: Int
)

fun PokemonInfoApiModel.asUiSate(): PokemonInfoUiState {
    return PokemonInfoUiState(
            id = this.id,
            name = this.name,
            types = this.types.map { it.type.name },
            height = this.height,
            weight = this.weight
        )
}