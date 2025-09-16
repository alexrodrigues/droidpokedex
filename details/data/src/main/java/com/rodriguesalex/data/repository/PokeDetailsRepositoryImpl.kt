package com.rodriguesalex.data.repository

import com.rodriguesalex.data.service.PokeDetailsService
import com.rodriguesalex.domain.repository.PokeDetailsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class PokeDetailsRepositoryImpl
@Inject
constructor(
    private val pokeDetailsService: PokeDetailsService,
) : PokeDetailsRepository {
    override suspend fun fetchPokemonDetails(
        id: Int,
    ) = pokeDetailsService.fetchPokemonDetails(id)
}
