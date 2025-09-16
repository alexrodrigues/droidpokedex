package com.rodriguesalex.domain.model

data class PokemonDetailsResponse(
    val id: Int,
    val name: String,
    val base_experience: Int,
    val height: Int,
    val weight: Int,
    val abilities: List<AbilitySlot>,
    val sprites: Sprites,
    val types: List<TypeSlot>,
    val stats: List<StatSlot>
)

data class AbilitySlot(
    val ability: NamedResource,
    val is_hidden: Boolean,
    val slot: Int
)

data class Sprites(
    val front_default: String?,
    val back_default: String?,
    val front_shiny: String?,
    val back_shiny: String?
)

data class TypeSlot(
    val slot: Int,
    val type: NamedResource
)

data class StatSlot(
    val base_stat: Int,
    val effort: Int,
    val stat: NamedResource
)

data class NamedResource(
    val name: String,
    val url: String
)