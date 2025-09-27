package com.rodriguesalex.details.domain.usecase

import com.rodriguesalex.details.domain.mapper.toDomain
import com.rodriguesalex.details.domain.model.PokemonDetails
import com.rodriguesalex.details.domain.repository.PokeDetailsRepository
import javax.inject.Inject

class GetPokeDetailsUseCase
    @Inject
    constructor(
        private val repository: PokeDetailsRepository,
    ) {
        suspend fun invoke(param: Params): PokemonDetails {
            return repository.fetchPokemonDetails(param.id).toDomain()
        }

        data class Params(
            val id: Int,
        )
    }
