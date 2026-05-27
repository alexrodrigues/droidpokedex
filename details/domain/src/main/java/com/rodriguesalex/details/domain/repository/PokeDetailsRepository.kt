package com.rodriguesalex.details.domain.repository

import com.rodriguesalex.details.domain.model.DetailsFetchOutcome
import com.rodriguesalex.details.domain.model.PokemonDetailsAggregate

interface PokeDetailsRepository {
    suspend fun fetchPokemonDetails(id: Int): DetailsFetchOutcome<PokemonDetailsAggregate>
}
