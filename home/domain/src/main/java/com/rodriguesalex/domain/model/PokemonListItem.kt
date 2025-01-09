package com.rodriguesalex.domain.model

import androidx.compose.ui.graphics.Color

data class PokemonListItem(
    val id: Int,
    val name: String,
    val pokemonImageUrl: String,
    val backgroundColor: Color,
    val baseExperience: Int,
    val height: Int,
    val isDefault: Boolean,
    val order: Int,
    val weight: Int,
    val abilities: List<AbilitySlot>,
    val forms: List<NamedAPIResource>,
    val gameIndices: List<GameIndex>,
    val heldItems: List<HeldItem>,
    val locationAreaEncounters: String,
    val moves: List<MoveSlot>,
    val species: NamedAPIResource,
    val sprites: Sprites,
    val stats: List<StatSlot>,
    val types: List<TypeSlot>
)

data class AbilitySlot(
    val isHidden: Boolean,
    val slot: Int,
    val ability: NamedAPIResource
)

data class NamedAPIResource(
    val name: String,
    val url: String
)

data class GameIndex(
    val gameIndex: Int,
    val version: NamedAPIResource
)

data class HeldItem(
    val item: NamedAPIResource,
    val versionDetails: List<HeldItemVersion>
)

data class HeldItemVersion(
    val version: NamedAPIResource,
    val rarity: Int
)

data class MoveSlot(
    val move: NamedAPIResource,
    val versionGroupDetails: List<MoveVersion>
)

data class MoveVersion(
    val moveLearnMethod: NamedAPIResource,
    val versionGroup: NamedAPIResource,
    val levelLearnedAt: Int
)

data class Sprites(
    val backDefault: String?,
    val backFemale: String?,
    val backShiny: String?,
    val backShinyFemale: String?,
    val frontDefault: String?,
    val frontFemale: String?,
    val frontShiny: String?,
    val frontShinyFemale: String?
)

data class StatSlot(
    val stat: NamedAPIResource,
    val effort: Int,
    val baseStat: Int
)

data class TypeSlot(
    val slot: Int,
    val type: NamedAPIResource
)