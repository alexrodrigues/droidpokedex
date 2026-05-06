package com.rodriguesalex.data.repository

import com.rodriguesalex.data.service.PokeHomeService
import com.rodriguesalex.domain.model.FetchOutcome
import com.rodriguesalex.domain.model.PokemonListDetailedItemResponse
import com.rodriguesalex.domain.model.PokemonListResponse
import com.rodriguesalex.domain.model.RemoteDataSource
import com.rodriguesalex.domain.repository.PokeHomeRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class PokeHomeRepositoryImpl
    @Inject
    constructor(
        private val pokeHomeService: PokeHomeService,
    ) : PokeHomeRepository {
        private val homeListCache = ConcurrentHashMap<String, PokemonListResponse>()
        private val searchCache = ConcurrentHashMap<String, PokemonListDetailedItemResponse>()

        override suspend fun fetchPokemonHome(
            limit: Int,
            offset: Int,
        ): FetchOutcome<PokemonListResponse> {
            val cacheKey = "$limit:$offset"
            return try {
                val listResponse = pokeHomeService.fetchPokemonList(limit, offset)
                val fetchedDetails =
                    coroutineScope {
                        listResponse.results.map { result ->
                            async {
                                pokeHomeService.fetchHomePokemonDetail(result.url)
                            }
                        }.awaitAll()
                    }
                val enriched = listResponse.copy(detailedResults = fetchedDetails)
                homeListCache[cacheKey] = enriched
                FetchOutcome(enriched, RemoteDataSource.NETWORK)
            } catch (e: Exception) {
                homeListCache[cacheKey]?.let { cached ->
                    FetchOutcome(cached, RemoteDataSource.CACHE)
                } ?: throw e
            }
        }

        override suspend fun searchPokemon(name: String): FetchOutcome<PokemonListDetailedItemResponse> {
            val key = name.lowercase().trim()
            return try {
                val result = pokeHomeService.searchPokemon(name)
                searchCache[key] = result
                FetchOutcome(result, RemoteDataSource.NETWORK)
            } catch (e: Exception) {
                searchCache[key]?.let { cached ->
                    FetchOutcome(cached, RemoteDataSource.CACHE)
                } ?: throw e
            }
        }
    }
