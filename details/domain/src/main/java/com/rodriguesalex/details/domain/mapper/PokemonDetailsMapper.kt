package com.rodriguesalex.details.domain.mapper

import com.rodriguesalex.details.domain.model.PokemonDetails
import com.rodriguesalex.details.domain.model.PokemonDetailsResponse

fun PokemonDetailsResponse.toDomain(): PokemonDetails {
    return PokemonDetails(
        id = id,
        name = name,
        baseExperience = base_experience,
        heightMeters = height / 10.0,
        weightKg = weight / 10.0,
        types = types.map { it.type.name },
        abilities = abilities.map { slot ->
            PokemonDetails.Ability(
                name = slot.ability.name,
                hidden = slot.is_hidden
            )
        },
        stats = stats.associate { slot ->
            slot.stat.name.lowercase() to slot.base_stat
        },
        sprites = PokemonDetails.Sprites(
            frontDefault = sprites.front_default,
            backDefault = sprites.back_default,
            frontShiny = sprites.front_shiny,
            backShiny = sprites.back_shiny
        )
    )
}