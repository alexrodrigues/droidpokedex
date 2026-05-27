package com.rodriguesalex.droidpokedex.details

import com.rodriguesalex.details.domain.model.PokemonDetails

private const val OFFICIAL_ARTWORK_BASE =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork"

private fun officialArtworkUrl(id: Int): String = "$OFFICIAL_ARTWORK_BASE/$id.png"

private fun emptySprites(): PokemonDetails.Sprites =
    PokemonDetails.Sprites(
        frontDefault = null,
        backDefault = null,
        frontShiny = null,
        backShiny = null,
    )

fun bulbasaurDetails(): PokemonDetails =
    PokemonDetails(
        id = 1,
        name = "bulbasaur",
        baseExperience = 64,
        heightMeters = 0.7,
        weightKg = 6.9,
        types = listOf("grass", "poison"),
        abilities = emptyList(),
        stats =
            mapOf(
                "hp" to 45,
                "attack" to 49,
                "defense" to 49,
                "special-attack" to 65,
                "special-defense" to 65,
                "speed" to 45,
            ),
        sprites = emptySprites(),
        officialArtworkUrl = officialArtworkUrl(1),
        genus = "The Seed Pokémon",
        flavorText =
            "For some time after its birth, it grows by gaining nourishment from the seed on its back.",
        evolutionChain =
            listOf(
                evolutionStage(1, "bulbasaur"),
                evolutionStage(2, "ivysaur"),
                evolutionStage(3, "venusaur"),
            ),
    )

fun charmanderDetails(): PokemonDetails =
    PokemonDetails(
        id = 4,
        name = "charmander",
        baseExperience = 62,
        heightMeters = 0.6,
        weightKg = 8.5,
        types = listOf("fire"),
        abilities = emptyList(),
        stats =
            mapOf(
                "hp" to 39,
                "attack" to 52,
                "defense" to 43,
                "special-attack" to 60,
                "special-defense" to 50,
                "speed" to 65,
            ),
        sprites = emptySprites(),
        officialArtworkUrl = officialArtworkUrl(4),
        genus = "The Lizard Pokémon",
        flavorText =
            "Obviously prefers hot places. When it rains, steam is said to spout from the tip of its tail.",
        evolutionChain = emptyList(),
    )

private fun evolutionStage(
    id: Int,
    name: String,
): PokemonDetails.EvolutionStage =
    PokemonDetails.EvolutionStage(
        id = id,
        name = name,
        spriteUrl = officialArtworkUrl(id),
    )
