@file:Suppress("ConstructorParameterNaming")

package com.rodriguesalex.domain.model

data class PokemonListDetailedItemResponse(
    val id: Int,
    val name: String,
    val base_experience: Int,
    val height: Int,
    val is_default: Boolean,
    val order: Int,
    val weight: Int,
    val abilities: List<AbilitySlotResponse>,
    val forms: List<NamedAPIResourceResponse>,
    val game_indices: List<GameIndexResponse>,
    val held_items: List<HeldItemResponse>,
    val location_area_encounters: String,
    val moves: List<MoveSlotResponse>,
    val species: NamedAPIResourceResponse,
    val sprites: SpritesResponse,
    val stats: List<StatSlotResponse>,
    val types: List<TypeSlotResponse>,
)

data class AbilitySlotResponse(
    val is_hidden: Boolean,
    val slot: Int,
    val ability: NamedAPIResourceResponse,
)

data class NamedAPIResourceResponse(
    val name: String,
    val url: String,
)

data class GameIndexResponse(
    val game_index: Int,
    val version: NamedAPIResourceResponse,
)

data class HeldItemResponse(
    val item: NamedAPIResourceResponse,
    val version_details: List<HeldItemVersionResponse>,
)

data class HeldItemVersionResponse(
    val version: NamedAPIResourceResponse,
    val rarity: Int,
)

data class MoveSlotResponse(
    val move: NamedAPIResourceResponse,
    val version_group_details: List<MoveVersionResponse>,
)

data class MoveVersionResponse(
    val move_learn_method: NamedAPIResourceResponse,
    val version_group: NamedAPIResourceResponse,
    val level_learned_at: Int,
)

data class SpritesResponse(
    val back_default: String?,
    val back_female: String?,
    val back_shiny: String?,
    val back_shiny_female: String?,
    val front_default: String?,
    val front_female: String?,
    val front_shiny: String?,
    val front_shiny_female: String?,
)

data class StatSlotResponse(
    val stat: NamedAPIResourceResponse,
    val effort: Int,
    val base_stat: Int,
)

data class TypeSlotResponse(
    val slot: Int,
    val type: NamedAPIResourceResponse,
)
