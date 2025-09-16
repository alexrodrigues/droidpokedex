package com.rodriguesalex.domain.repository

import com.rodriguesalex.domain.model.PokemonDetailsResponse

interface PokeDetailsRepository {
    suspend fun fetchPokemonDetails(
        id: Int,
    ): PokemonDetailsResponse
}
