package com.rodriguesalex.data.repository

import com.rodriguesalex.data.service.PokeHomeService
import com.rodriguesalex.domain.model.PokemonListResponse
import com.rodriguesalex.domain.repository.PokeHomeRepository
import javax.inject.Inject

class PokeHomeRepositoryImpl @Inject constructor(
    private val pokeHomeService: PokeHomeService
) : PokeHomeRepository {

    override suspend fun fetchPokemonHome(limit: Int, offset: Int): PokemonListResponse =
        pokeHomeService.fetchPokemonList(limit, offset)

}