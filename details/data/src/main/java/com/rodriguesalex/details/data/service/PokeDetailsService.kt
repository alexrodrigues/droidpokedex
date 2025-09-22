package com.rodriguesalex.details.data.service

import com.rodriguesalex.details.domain.model.PokemonDetailsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeDetailsService {
    @GET("pokemon/{id}")
    suspend fun fetchPokemonDetails(
        @Path("id") id: Int,
    ): PokemonDetailsResponse
}
