package com.rodriguesalex.domain.usecase

import com.rodriguesalex.domain.mapper.toModel
import com.rodriguesalex.domain.model.PokemonList
import com.rodriguesalex.domain.repository.PokeHomeRepository
import javax.inject.Inject

class GetPokeHomeUseCase @Inject constructor(
    private val repository: PokeHomeRepository
) {
    suspend fun invoke(param: Params): PokemonList {
        return repository.fetchPokemonHome(param.limit, param.offset).toModel()
    }

    data class Params(
        val limit: Int,
        val offset: Int
    )
}