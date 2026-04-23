package org.sabaini.pokedex.data.mapper

import org.sabaini.pokedex.data.local.PokemonInfoLocalModel
import org.sabaini.pokedex.data.local.PokemonLocalModel
import org.sabaini.pokedex.domain.model.Pokemon
import org.sabaini.pokedex.domain.model.PokemonInfo
import org.sabaini.pokedex.util.Constants.COMMA

fun PokemonLocalModel.toDomain(): Pokemon {
    return Pokemon(
        page = this.page,
        name = this.name,
        url = this.url,
        backgroundColor = this.backgroundColor
    )
}

fun List<PokemonLocalModel>.toDomain(): List<Pokemon> = map { it.toDomain() }

fun PokemonInfoLocalModel?.toDomain(): PokemonInfo? {
    return this?.let {
        PokemonInfo(
            id = it.id,
            name = it.name,
            types = it.types.split(COMMA),
            description = it.description,
            height = it.height,
            weight = it.weight,
            backgroundColor = null,
            baseStats = listOf(),
            evolutionChain = listOf(),
        )
    }
}
