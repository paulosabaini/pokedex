package org.sabaini.pokedex.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.sabaini.pokedex.util.Constants.ZERO

@Entity
data class PokemonLocalModel(
    var page: Int = ZERO,
    @PrimaryKey
    val name: String,
    val url: String,
    val backgroundColor: Int? = null,
)

@Entity
data class PokemonInfoLocalModel(
    @PrimaryKey
    val id: Int,
    val name: String,
    val types: String,
    val description: String,
    val height: Int,
    val weight: Int,
    val evolutionChainId: String,
)

@Entity(primaryKeys = ["idPokemon", "name", "baseState"])
data class PokemonInfoStatLocalModel(
    val idPokemon: Int,
    val name: String,
    val baseState: Int,
)

@Entity(primaryKeys = ["evolutionChainId", "idPokemon", "minLevel"])
data class PokemonInfoEvolutionLocalModel(
    val evolutionChainId: Int,
    val idPokemon: Int,
    val minLevel: Int,
)

@Entity
data class GenerationLocalModel(
    @PrimaryKey
    val name: String,
    val url: String,
    val displayName: String
)
