package com.rodriguesalex.details.domain.repository

import com.rodriguesalex.details.domain.model.PokemonDetailsResponse

interface PokeDetailsRepository {
    suspend fun fetchPokemonDetails(
        id: Int,
    ): PokemonDetailsResponse
}
