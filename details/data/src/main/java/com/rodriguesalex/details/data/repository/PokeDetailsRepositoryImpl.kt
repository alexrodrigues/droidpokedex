package com.rodriguesalex.details.data.repository

import com.rodriguesalex.details.data.service.PokeDetailsService
import com.rodriguesalex.details.domain.model.DetailsFetchOutcome
import com.rodriguesalex.details.domain.model.DetailsRemoteSource
import com.rodriguesalex.details.domain.model.PokemonDetailsResponse
import com.rodriguesalex.details.domain.repository.PokeDetailsRepository
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class PokeDetailsRepositoryImpl
    @Inject
    constructor(
        private val pokeDetailsService: PokeDetailsService,
    ) : PokeDetailsRepository {
        private val cache = ConcurrentHashMap<Int, PokemonDetailsResponse>()

        override suspend fun fetchPokemonDetails(id: Int): DetailsFetchOutcome<PokemonDetailsResponse> {
            return try {
                val response = pokeDetailsService.fetchPokemonDetails(id)
                cache[id] = response
                DetailsFetchOutcome(response, DetailsRemoteSource.NETWORK)
            } catch (e: CancellationException) {
                throw e
            } catch (e: IOException) {
                recoverFromCache(id, e)
            } catch (e: HttpException) {
                recoverFromCache(id, e)
            }
        }

        private fun recoverFromCache(
            id: Int,
            error: Throwable,
        ): DetailsFetchOutcome<PokemonDetailsResponse> =
            cache[id]?.let { cached ->
                DetailsFetchOutcome(cached, DetailsRemoteSource.CACHE)
            } ?: throw error
    }
