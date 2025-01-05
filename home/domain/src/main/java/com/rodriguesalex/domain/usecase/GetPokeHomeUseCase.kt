package com.rodriguesalex.domain.usecase

import com.rodriguesalex.domain.model.PokemonListResponse
import com.rodriguesalex.domain.repository.PokeHomeRepository
import javax.inject.Inject

class GetPokeHomeUseCase @Inject constructor(
    private val repository: PokeHomeRepository
) {
    suspend fun invoke(param: Params): PokemonListResponse {
        return repository.fetchPokemonHome(param.limit, param.offset)
    }

    data class Params(
        val limit: Int,
        val offset: Int
    )
}