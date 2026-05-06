package com.rodriguesalex.details.domain.usecase

import com.rodriguesalex.details.domain.mapper.toDomain
import com.rodriguesalex.details.domain.model.DetailsFetchOutcome
import com.rodriguesalex.details.domain.model.PokemonDetails
import com.rodriguesalex.details.domain.repository.PokeDetailsRepository
import javax.inject.Inject

class GetPokeDetailsUseCase
    @Inject
    constructor(
        private val repository: PokeDetailsRepository,
    ) {
        suspend fun invoke(param: Params): DetailsFetchOutcome<PokemonDetails> {
            val outcome = repository.fetchPokemonDetails(param.id)
            return DetailsFetchOutcome(
                value = outcome.value.toDomain(),
                source = outcome.source,
            )
        }

        data class Params(
            val id: Int,
        )
    }
