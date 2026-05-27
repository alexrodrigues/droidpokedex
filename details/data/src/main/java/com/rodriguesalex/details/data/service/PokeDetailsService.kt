package com.rodriguesalex.details.data.service

import com.rodriguesalex.details.domain.model.EvolutionChainResponse
import com.rodriguesalex.details.domain.model.PokemonDetailsResponse
import com.rodriguesalex.details.domain.model.PokemonSpeciesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface PokeDetailsService {
    @GET("pokemon/{id}")
    suspend fun fetchPokemonDetails(
        @Path("id") id: Int,
    ): PokemonDetailsResponse

    @GET("pokemon-species/{id}")
    suspend fun fetchPokemonSpecies(
        @Path("id") id: Int,
    ): PokemonSpeciesResponse

    @GET
    suspend fun fetchEvolutionChain(
        @Url url: String,
    ): EvolutionChainResponse
}
