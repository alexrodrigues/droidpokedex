package com.rodriguesalex.droidpokedex.network.services

import com.rodriguesalex.droidpokedex.network.data.PokemonListResponse
import retrofit2.http.GET

interface PokeHomeService {
    @GET("pokemon")
    suspend fun fetchPokemonList(
        @retrofit2.http.Query("limit") limit: Int,
        @retrofit2.http.Query("offset") offset: Int
    ): PokemonListResponse
}