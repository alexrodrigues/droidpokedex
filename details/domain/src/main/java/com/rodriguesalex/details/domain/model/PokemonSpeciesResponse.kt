package com.rodriguesalex.details.domain.model

data class PokemonSpeciesResponse(
    val id: Int,
    val name: String,
    val genera: List<GenusEntry>,
    val flavor_text_entries: List<FlavorTextEntry>,
    val evolution_chain: ApiResourceLink?,
)

data class GenusEntry(
    val genus: String,
    val language: NamedResource,
)

data class FlavorTextEntry(
    val flavor_text: String,
    val language: NamedResource,
    val version: NamedResource,
)

data class ApiResourceLink(
    val url: String,
)
