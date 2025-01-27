package com.rodriguesalex.domain.mapper

import com.rodriguesalex.domain.model.AbilitySlot
import com.rodriguesalex.domain.model.AbilitySlotResponse
import com.rodriguesalex.domain.model.DroidPokemonTypeColor
import com.rodriguesalex.domain.model.GameIndex
import com.rodriguesalex.domain.model.GameIndexResponse
import com.rodriguesalex.domain.model.HeldItem
import com.rodriguesalex.domain.model.HeldItemResponse
import com.rodriguesalex.domain.model.HeldItemVersion
import com.rodriguesalex.domain.model.HeldItemVersionResponse
import com.rodriguesalex.domain.model.MoveSlot
import com.rodriguesalex.domain.model.MoveSlotResponse
import com.rodriguesalex.domain.model.MoveVersion
import com.rodriguesalex.domain.model.MoveVersionResponse
import com.rodriguesalex.domain.model.NamedAPIResource
import com.rodriguesalex.domain.model.NamedAPIResourceResponse
import com.rodriguesalex.domain.model.PokemonList
import com.rodriguesalex.domain.model.PokemonListDetailedItemResponse
import com.rodriguesalex.domain.model.PokemonListItem
import com.rodriguesalex.domain.model.PokemonListResponse
import com.rodriguesalex.domain.model.Sprites
import com.rodriguesalex.domain.model.SpritesResponse
import com.rodriguesalex.domain.model.StatSlot
import com.rodriguesalex.domain.model.StatSlotResponse
import com.rodriguesalex.domain.model.TypeSlot
import com.rodriguesalex.domain.model.TypeSlotResponse

fun PokemonListResponse.toModel(): PokemonList =
    PokemonList(
        count = count,
        next = next,
        previous = previous,
        results = detailedResults?.map { it.toModel() }.orEmpty(),
    )

fun PokemonListDetailedItemResponse.toModel(): PokemonListItem =
    PokemonListItem(
        id = id,
        name = name,
        pokemonImageUrl =
            "https://raw.githubusercontent.com" +
                "/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png",
        backgroundColor =
            DroidPokemonTypeColor.getPokemonColor(
                types.firstOrNull()?.type?.name ?: "normal",
            ).primary,
        baseExperience = base_experience,
        height = height,
        isDefault = is_default,
        order = order,
        weight = weight,
        abilities = abilities.map { it.toModel() },
        forms = forms.map { it.toModel() },
        gameIndices = game_indices.map { it.toModel() },
        heldItems = held_items.map { it.toModel() },
        locationAreaEncounters = location_area_encounters,
        moves = moves.map { it.toModel() },
        species = species.toModel(),
        sprites = sprites.toModel(),
        stats = stats.map { it.toModel() },
        types = types.map { it.toModel() },
    )

fun AbilitySlotResponse.toModel() =
    AbilitySlot(
        isHidden = is_hidden,
        slot = slot,
        ability = ability.toModel(),
    )

fun NamedAPIResourceResponse.toModel() =
    NamedAPIResource(
        name = name,
        url = url,
    )

fun GameIndexResponse.toModel() =
    GameIndex(
        gameIndex = game_index,
        version = version.toModel(),
    )

fun HeldItemResponse.toModel() =
    HeldItem(
        item = item.toModel(),
        versionDetails = version_details.map { it.toModel() },
    )

fun HeldItemVersionResponse.toModel() =
    HeldItemVersion(
        version = version.toModel(),
        rarity = rarity,
    )

fun MoveSlotResponse.toModel() =
    MoveSlot(
        move = move.toModel(),
        versionGroupDetails = version_group_details.map { it.toModel() },
    )

fun MoveVersionResponse.toModel() =
    MoveVersion(
        moveLearnMethod = move_learn_method.toModel(),
        versionGroup = version_group.toModel(),
        levelLearnedAt = level_learned_at,
    )

fun SpritesResponse.toModel() =
    Sprites(
        backDefault = back_default,
        backFemale = back_female,
        backShiny = back_shiny,
        backShinyFemale = back_shiny_female,
        frontDefault = front_default,
        frontFemale = front_female,
        frontShiny = front_shiny,
        frontShinyFemale = front_shiny_female,
    )

fun StatSlotResponse.toModel() =
    StatSlot(
        stat = stat.toModel(),
        effort = effort,
        baseStat = base_stat,
    )

fun TypeSlotResponse.toModel() =
    TypeSlot(
        slot = slot,
        type = type.toModel(),
    )

private fun extractIdFromUrl(url: String): String? {
    val regex = ".*/(\\d+)/?$".toRegex()
    val matchResult = regex.find(url)
    return matchResult?.groupValues?.get(1)
}
