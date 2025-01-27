package com.rodriguesalex.domain.model

data class PokemonListResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonListItemResponse>,
    var detailedResults: List<PokemonListDetailedItemResponse>? = null,
)
