package com.rodriguesalex.domain.usecase

import com.rodriguesalex.domain.mapper.toModel
import com.rodriguesalex.domain.model.PokemonListItem
import com.rodriguesalex.domain.repository.PokeHomeRepository
import javax.inject.Inject

class SearchPokemonUseCase
    @Inject
    constructor(
        private val repository: PokeHomeRepository,
    ) {
        suspend fun invoke(param: Params): PokemonListItem = repository.searchPokemon(param.name).toModel()

        data class Params(
            val name: String,
        )
    }
