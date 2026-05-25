package com.rodriguesalex.details.domain.mapper

import com.rodriguesalex.details.domain.model.ChainLink
import com.rodriguesalex.details.domain.model.EvolutionChainResponse
import com.rodriguesalex.details.domain.model.NamedResource
import com.rodriguesalex.details.domain.model.PokemonDetails
import com.rodriguesalex.details.domain.model.PokemonDetailsAggregate
import com.rodriguesalex.details.domain.model.PokemonDetailsResponse
import com.rodriguesalex.details.domain.model.PokemonSpeciesResponse

private const val ENGLISH_LANGUAGE = "en"
private const val OFFICIAL_ARTWORK_BASE =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork"

fun PokemonDetailsAggregate.toDomain(): PokemonDetails {
    return pokemon.toDomain(
        genus = species?.englishGenus(),
        flavorText = species?.englishFlavorText(),
        evolutionChain = evolutionChain?.toEvolutionStages().orEmpty(),
    )
}

fun PokemonDetailsResponse.toDomain(
    genus: String? = null,
    flavorText: String? = null,
    evolutionChain: List<PokemonDetails.EvolutionStage> = emptyList(),
): PokemonDetails {
    return PokemonDetails(
        id = id,
        name = name,
        baseExperience = base_experience,
        heightMeters = height / 10.0,
        weightKg = weight / 10.0,
        types = types.map { it.type.name },
        abilities =
            abilities.map { slot ->
                PokemonDetails.Ability(
                    name = slot.ability.name,
                    hidden = slot.is_hidden,
                )
            },
        stats =
            stats.associate { slot ->
                slot.stat.name.lowercase() to slot.base_stat
            },
        sprites =
            PokemonDetails.Sprites(
                frontDefault = sprites.front_default,
                backDefault = sprites.back_default,
                frontShiny = sprites.front_shiny,
                backShiny = sprites.back_shiny,
            ),
        officialArtworkUrl = "$OFFICIAL_ARTWORK_BASE/$id.png",
        genus = genus,
        flavorText = flavorText,
        evolutionChain = evolutionChain,
    )
}

private fun PokemonSpeciesResponse.englishGenus(): String? =
    genera
        .firstOrNull { it.language.name == ENGLISH_LANGUAGE }
        ?.genus
        ?.replaceFirstChar { char ->
            if (char.isLowerCase()) char.titlecase() else char.toString()
        }

private fun PokemonSpeciesResponse.englishFlavorText(): String? =
    flavor_text_entries
        .firstOrNull { it.language.name == ENGLISH_LANGUAGE }
        ?.flavor_text
        ?.replace("\n", " ")
        ?.replace("\u000c", " ")
        ?.trim()

private fun EvolutionChainResponse.toEvolutionStages(): List<PokemonDetails.EvolutionStage> =
    flattenChain(chain).map { species ->
        val speciesId = species.extractSpeciesId()
        PokemonDetails.EvolutionStage(
            id = speciesId,
            name = species.name,
            spriteUrl = "$OFFICIAL_ARTWORK_BASE/$speciesId.png",
        )
    }

private fun flattenChain(link: ChainLink): List<NamedResource> {
    val result = mutableListOf(link.species)
    link.evolves_to.forEach { child ->
        result.addAll(flattenChain(child))
    }
    return result
}

private fun NamedResource.extractSpeciesId(): Int {
    val idSegment = url.trimEnd('/').substringAfterLast('/')
    return idSegment.toIntOrNull() ?: 0
}
