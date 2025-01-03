package com.rodriguesalex.droidpokedex.network.data

data class PokemonListResponse (
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonListItemResponse>
)