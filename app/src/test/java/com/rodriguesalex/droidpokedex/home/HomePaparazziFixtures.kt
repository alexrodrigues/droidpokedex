package com.rodriguesalex.droidpokedex.home

import androidx.compose.ui.graphics.Color
import com.rodriguesalex.domain.model.NamedAPIResource
import com.rodriguesalex.domain.model.PokemonListItem
import com.rodriguesalex.domain.model.Sprites
import com.rodriguesalex.domain.model.StatSlot
import com.rodriguesalex.domain.model.TypeSlot

private const val ARTWORK_BASE =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork"

fun homePokemonFixtures(): List<PokemonListItem> =
    listOf(
        homePokemonItem(id = 1, name = "bulbasaur", type = "grass", color = Color(0xFF62B957)),
        homePokemonItem(id = 4, name = "charmander", type = "fire", color = Color(0xFFFD7D24)),
        homePokemonItem(id = 7, name = "squirtle", type = "water", color = Color(0xFF4A90DA)),
    )

private fun homePokemonItem(
    id: Int,
    name: String,
    type: String,
    color: Color,
): PokemonListItem =
    PokemonListItem(
        id = id,
        name = name,
        pokemonImageUrl = "$ARTWORK_BASE/$id.png",
        backgroundColor = color,
        baseExperience = 64,
        height = 7,
        isDefault = true,
        order = id,
        weight = 69,
        abilities = emptyList(),
        forms = emptyList(),
        gameIndices = emptyList(),
        heldItems = emptyList(),
        locationAreaEncounters = "",
        moves = emptyList(),
        species = NamedAPIResource(name, "https://pokeapi.co/api/v2/pokemon-species/$id/"),
        sprites =
            Sprites(
                frontDefault = null,
                backDefault = null,
                backFemale = null,
                backShiny = null,
                backShinyFemale = null,
                frontFemale = null,
                frontShiny = null,
                frontShinyFemale = null,
            ),
        stats =
            listOf(
                StatSlot(
                    stat = NamedAPIResource("hp", "https://pokeapi.co/api/v2/stat/1/"),
                    effort = 0,
                    baseStat = 45,
                ),
            ),
        types =
            listOf(
                TypeSlot(
                    slot = 1,
                    type = NamedAPIResource(type, "https://pokeapi.co/api/v2/type/1/"),
                ),
            ),
    )
