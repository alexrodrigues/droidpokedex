package com.rodriguesalex.details.domain.model

data class PokemonDetailsAggregate(
    val pokemon: PokemonDetailsResponse,
    val species: PokemonSpeciesResponse?,
    val evolutionChain: EvolutionChainResponse?,
)
