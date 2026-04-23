package org.sabaini.pokedex.domain.model

data class Pokemon(
    val page: Int,
    val name: String,
    val url: String,
    val backgroundColor: Int? = null,
)
