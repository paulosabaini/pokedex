package org.sabaini.pokedex.data.remote

import com.google.gson.annotations.SerializedName

data class GenerationListApiModel(
    val results: List<GenerationApiModel>
)

data class GenerationApiModel(
    val name: String,
    val url: String
)

data class GenerationDetailApiModel(
    @SerializedName("pokemon_species")
    val pokemonSpecies: List<PokemonSpeciesApiModel>
)

data class PokemonSpeciesApiModel(
    val name: String,
    val url: String
)
