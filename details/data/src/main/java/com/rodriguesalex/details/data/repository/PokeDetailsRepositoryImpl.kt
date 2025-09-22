package com.rodriguesalex.details.data.repository

import com.rodriguesalex.details.data.service.PokeDetailsService
import com.rodriguesalex.details.domain.repository.PokeDetailsRepository
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
