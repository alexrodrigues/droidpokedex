package com.rodriguesalex.domain.repository

import com.rodriguesalex.domain.model.PokemonListResponse

interface PokeHomeRepository {
    suspend fun fetchPokemonHome(limit: Int, offset: Int): PokemonListResponse
}