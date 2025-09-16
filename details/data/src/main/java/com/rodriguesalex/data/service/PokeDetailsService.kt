package com.rodriguesalex.data.service

import com.rodriguesalex.domain.model.PokemonDetailsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PokeDetailsService {
    @GET("pokemon/{id}")
    suspend fun fetchPokemonDetails(
        @Query("{id}") id: Int,
    ): PokemonDetailsResponse
}
