package com.rodriguesalex.data.repository

import com.rodriguesalex.data.service.PokeHomeService
import com.rodriguesalex.domain.model.PokemonListDetailedItemResponse
import com.rodriguesalex.domain.model.PokemonListResponse
import com.rodriguesalex.domain.repository.PokeHomeRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class PokeHomeRepositoryImpl
    @Inject
    constructor(
        private val pokeHomeService: PokeHomeService,
    ) : PokeHomeRepository {
        override suspend fun fetchPokemonHome(
            limit: Int,
            offset: Int,
        ): PokemonListResponse {
            val pokemonListResponse = pokeHomeService.fetchPokemonList(limit, offset)

            val detailedResults =
                coroutineScope {
                    pokemonListResponse.results.map { result ->
                        async {
                            pokeHomeService.fetchHomePokemonDetail(result.url)
                        }
                    }.awaitAll()
                }

            return pokemonListResponse.apply {
                this.detailedResults = detailedResults
            }
        }

        override suspend fun searchPokemon(name: String): PokemonListDetailedItemResponse {
            return pokeHomeService.searchPokemon(name)
        }
    }
