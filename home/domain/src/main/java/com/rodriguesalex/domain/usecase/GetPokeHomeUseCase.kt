package com.rodriguesalex.domain.usecase

import com.rodriguesalex.domain.mapper.toModel
import com.rodriguesalex.domain.model.FetchOutcome
import com.rodriguesalex.domain.model.PokemonList
import com.rodriguesalex.domain.repository.PokeHomeRepository
import javax.inject.Inject

class GetPokeHomeUseCase
    @Inject
    constructor(
        private val repository: PokeHomeRepository,
    ) {
        suspend fun invoke(param: Params): FetchOutcome<PokemonList> {
            val outcome = repository.fetchPokemonHome(param.limit, param.offset)
            return FetchOutcome(
                value = outcome.value.toModel(),
                source = outcome.source,
            )
        }

        data class Params(
            val limit: Int,
            val offset: Int,
        )
    }
