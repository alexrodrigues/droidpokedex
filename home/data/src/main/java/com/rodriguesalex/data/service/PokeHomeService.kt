package com.rodriguesalex.data.service

import com.rodriguesalex.domain.model.PokemonListDetailedItemResponse
import com.rodriguesalex.domain.model.PokemonListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface PokeHomeService {
    @GET("pokemon")
    suspend fun fetchPokemonList(
        @retrofit2.http.Query("limit") limit: Int,
        @retrofit2.http.Query("offset") offset: Int,
    ): PokemonListResponse

    @GET
    suspend fun fetchHomePokemonDetail(
        @Url url: String,
    ): PokemonListDetailedItemResponse

    @GET("pokemon/{name}")
    suspend fun searchPokemon(
        @Path("name") name: String,
    ): PokemonListDetailedItemResponse
}
