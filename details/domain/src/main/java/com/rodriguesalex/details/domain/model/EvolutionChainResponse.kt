package com.rodriguesalex.details.domain.model

data class EvolutionChainResponse(
    val id: Int,
    val chain: ChainLink,
)

data class ChainLink(
    val species: NamedResource,
    val evolves_to: List<ChainLink> = emptyList(),
)
