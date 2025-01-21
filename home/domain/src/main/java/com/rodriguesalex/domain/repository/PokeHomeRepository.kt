package com.rodriguesalex.domain.repository

import com.rodriguesalex.domain.model.PokemonListDetailedItemResponse
import com.rodriguesalex.domain.model.PokemonListResponse

interface PokeHomeRepository {
    suspend fun fetchPokemonHome(limit: Int, offset: Int): PokemonListResponse
    suspend fun searchPokemon(name: String): PokemonListDetailedItemResponse
}