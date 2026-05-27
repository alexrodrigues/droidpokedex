package com.rodriguesalex.details.data

import com.rodriguesalex.details.domain.model.AbilitySlot
import com.rodriguesalex.details.domain.model.ApiResourceLink
import com.rodriguesalex.details.domain.model.ChainLink
import com.rodriguesalex.details.domain.model.EvolutionChainResponse
import com.rodriguesalex.details.domain.model.GenusEntry
import com.rodriguesalex.details.domain.model.NamedResource
import com.rodriguesalex.details.domain.model.PokemonDetailsResponse
import com.rodriguesalex.details.domain.model.PokemonSpeciesResponse
import com.rodriguesalex.details.domain.model.Sprites
import com.rodriguesalex.details.domain.model.StatSlot
import com.rodriguesalex.details.domain.model.TypeSlot

internal fun testPokemonResponse(): PokemonDetailsResponse =
    PokemonDetailsResponse(
        id = 1,
        name = "bulbasaur",
        base_experience = 64,
        height = 7,
        weight = 69,
        abilities =
            listOf(
                AbilitySlot(
                    ability = NamedResource("overgrow", "https://pokeapi.co/api/v2/ability/65/"),
                    is_hidden = false,
                    slot = 1,
                ),
            ),
        sprites = Sprites("https://pokeapi.co/sprites/pokemon/1.png", null, null, null),
        types = listOf(TypeSlot(1, NamedResource("grass", "https://pokeapi.co/api/v2/type/12/"))),
        stats = listOf(StatSlot(45, 0, NamedResource("hp", "https://pokeapi.co/api/v2/stat/1/"))),
    )

internal fun testSpeciesResponse(): PokemonSpeciesResponse =
    PokemonSpeciesResponse(
        id = 1,
        name = "bulbasaur",
        genera =
            listOf(
                GenusEntry(
                    genus = "the Seed Pokémon",
                    language = NamedResource("en", "https://pokeapi.co/api/v2/language/9/"),
                ),
            ),
        flavor_text_entries = emptyList(),
        evolution_chain = ApiResourceLink("https://pokeapi.co/api/v2/evolution-chain/1/"),
    )

internal fun testSpeciesResponseWithoutEvolution(): PokemonSpeciesResponse = testSpeciesResponse().copy(evolution_chain = null)

internal fun testEvolutionChainResponse(): EvolutionChainResponse =
    EvolutionChainResponse(
        id = 1,
        chain =
            ChainLink(
                species = NamedResource("bulbasaur", "https://pokeapi.co/api/v2/pokemon-species/1/"),
                evolves_to =
                    listOf(
                        ChainLink(
                            species = NamedResource("ivysaur", "https://pokeapi.co/api/v2/pokemon-species/2/"),
                        ),
                    ),
            ),
    )
