package org.sabaini.pokedex.domain.model

data class PokemonInfo(
    val id: Int,
    val name: String,
    val types: List<String>,
    val description: String,
    val height: Int,
    val weight: Int,
    val backgroundColor: Int? = null,
    val baseStats: List<PokemonStat>,
    val evolutionChain: List<PokemonEvolution>,
)

data class PokemonStat(
    val name: String,
    val baseState: Int,
)

data class PokemonEvolution(
    val pokemon: PokemonInfo,
    val minLevel: Int,
)
