package com.rodriguesalex.details.domain.usecase

import com.rodriguesalex.details.domain.model.PokemonDetailsResponse
import com.rodriguesalex.details.domain.repository.PokeDetailsRepository
import javax.inject.Inject

class GetPokeDetailsUseCase
    @Inject
    constructor(
        private val repository: PokeDetailsRepository,
    ) {
        suspend fun invoke(param: Params): PokemonDetailsResponse {
            return repository.fetchPokemonDetails(param.id)
        }

        data class Params(
            val id: Int,
        )
    }
