package com.rodriguesalex.details.data.repository

import com.rodriguesalex.details.data.service.PokeDetailsService
import com.rodriguesalex.details.domain.model.DetailsFetchOutcome
import com.rodriguesalex.details.domain.model.DetailsRemoteSource
import com.rodriguesalex.details.domain.model.PokemonDetailsAggregate
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
        private val cache = ConcurrentHashMap<Int, PokemonDetailsAggregate>()

        override suspend fun fetchPokemonDetails(id: Int): DetailsFetchOutcome<PokemonDetailsAggregate> {
            return try {
                val aggregate = fetchFromNetwork(id)
                cache[id] = aggregate
                DetailsFetchOutcome(aggregate, DetailsRemoteSource.NETWORK)
            } catch (e: CancellationException) {
                throw e
            } catch (e: IOException) {
                recoverFromCache(id, e)
            } catch (e: HttpException) {
                recoverFromCache(id, e)
            }
        }

        private suspend fun fetchFromNetwork(id: Int): PokemonDetailsAggregate {
            val pokemon = pokeDetailsService.fetchPokemonDetails(id)
            val species =
                runCatching { pokeDetailsService.fetchPokemonSpecies(id) }
                    .getOrNull()
            val evolutionChain =
                species?.evolution_chain?.url?.let { evolutionUrl ->
                    runCatching { pokeDetailsService.fetchEvolutionChain(evolutionUrl) }
                        .getOrNull()
                }
            return PokemonDetailsAggregate(
                pokemon = pokemon,
                species = species,
                evolutionChain = evolutionChain,
            )
        }

        private fun recoverFromCache(
            id: Int,
            error: Throwable,
        ): DetailsFetchOutcome<PokemonDetailsAggregate> =
            cache[id]?.let { cached ->
                DetailsFetchOutcome(cached, DetailsRemoteSource.CACHE)
            } ?: throw error
    }
