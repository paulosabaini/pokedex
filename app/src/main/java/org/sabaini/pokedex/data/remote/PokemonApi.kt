package org.sabaini.pokedex.data.remote

import org.sabaini.pokedex.util.Constants.PAGING_SIZE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApi {

    @GET("pokemon")
    suspend fun fetchPokemonList(
        @Query("limit") limit: Int = PAGING_SIZE,
        @Query("offset") offset: Int = 0,
    ): PokemonListApiModel

    @GET("pokemon/{name}")
    suspend fun fetchPokemonInfo(@Path("name") name: String): PokemonInfoApiModel

    @GET("pokemon-species/{id}")
    suspend fun fetchPokemonInfoSpecies(@Path("id") id: String): PokemonInfoSpeciesApiModel

    @GET("evolution-chain/{id}")
    suspend fun fetchPokemonInfoEvolutions(@Path("id") id: String): PokemonInfoEvolutionApiModel

    @GET("generation")
    suspend fun fetchGenerationList(): GenerationListApiModel

    @GET("generation/{name}")
    suspend fun fetchGenerationDetail(@Path("name") name: String): GenerationDetailApiModel
}
