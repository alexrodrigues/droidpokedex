package com.rodriguesalex.details.domain

import com.rodriguesalex.details.domain.model.AbilitySlot
import com.rodriguesalex.details.domain.model.ApiResourceLink
import com.rodriguesalex.details.domain.model.ChainLink
import com.rodriguesalex.details.domain.model.EvolutionChainResponse
import com.rodriguesalex.details.domain.model.FlavorTextEntry
import com.rodriguesalex.details.domain.model.GenusEntry
import com.rodriguesalex.details.domain.model.NamedResource
import com.rodriguesalex.details.domain.model.PokemonDetailsAggregate
import com.rodriguesalex.details.domain.model.PokemonDetailsResponse
import com.rodriguesalex.details.domain.model.PokemonSpeciesResponse
import com.rodriguesalex.details.domain.model.Sprites
import com.rodriguesalex.details.domain.model.StatSlot
import com.rodriguesalex.details.domain.model.TypeSlot

private val englishLanguage =
    NamedResource(name = "en", url = "https://pokeapi.co/api/v2/language/9/")
private val japaneseLanguage =
    NamedResource(name = "ja", url = "https://pokeapi.co/api/v2/language/11/")
private val redVersion =
    NamedResource(name = "red", url = "https://pokeapi.co/api/v2/version/1/")

fun bulbasaurPokemonResponse(): PokemonDetailsResponse =
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
        sprites =
            Sprites(
                front_default = "https://pokeapi.co/sprites/pokemon/1.png",
                back_default = null,
                front_shiny = null,
                back_shiny = null,
            ),
        types =
            listOf(
                TypeSlot(1, NamedResource("grass", "https://pokeapi.co/api/v2/type/12/")),
                TypeSlot(2, NamedResource("poison", "https://pokeapi.co/api/v2/type/4/")),
            ),
        stats =
            listOf(
                statSlot("hp", 45),
                statSlot("attack", 49),
                statSlot("defense", 49),
                statSlot("special-attack", 65),
                statSlot("special-defense", 65),
                statSlot("speed", 45),
            ),
    )

fun bulbasaurSpeciesResponse(): PokemonSpeciesResponse =
    PokemonSpeciesResponse(
        id = 1,
        name = "bulbasaur",
        genera =
            listOf(
                GenusEntry(genus = "the Seed Pokémon", language = englishLanguage),
            ),
        flavor_text_entries =
            listOf(
                FlavorTextEntry(
                    flavor_text = "For some time after its birth,\nit grows by gaining nourishment\u000cfrom the seed on its back.",
                    language = englishLanguage,
                    version = redVersion,
                ),
            ),
        evolution_chain =
            ApiResourceLink(url = "https://pokeapi.co/api/v2/evolution-chain/1/"),
    )

fun bulbasaurSpeciesResponseWithoutEvolution(): PokemonSpeciesResponse = bulbasaurSpeciesResponse().copy(evolution_chain = null)

fun japaneseOnlySpeciesResponse(): PokemonSpeciesResponse =
    PokemonSpeciesResponse(
        id = 1,
        name = "bulbasaur",
        genera = listOf(GenusEntry(genus = "たねポケモン", language = japaneseLanguage)),
        flavor_text_entries =
            listOf(
                FlavorTextEntry(
                    flavor_text = "Japanese only",
                    language = japaneseLanguage,
                    version = redVersion,
                ),
            ),
        evolution_chain = null,
    )

fun bulbasaurEvolutionChainResponse(): EvolutionChainResponse =
    EvolutionChainResponse(
        id = 1,
        chain =
            ChainLink(
                species =
                    NamedResource(
                        name = "bulbasaur",
                        url = "https://pokeapi.co/api/v2/pokemon-species/1/",
                    ),
                evolves_to =
                    listOf(
                        ChainLink(
                            species =
                                NamedResource(
                                    name = "ivysaur",
                                    url = "https://pokeapi.co/api/v2/pokemon-species/2/",
                                ),
                            evolves_to =
                                listOf(
                                    ChainLink(
                                        species =
                                            NamedResource(
                                                name = "venusaur",
                                                url = "https://pokeapi.co/api/v2/pokemon-species/3/",
                                            ),
                                    ),
                                ),
                        ),
                    ),
            ),
    )

fun branchedEvolutionChainResponse(): EvolutionChainResponse =
    EvolutionChainResponse(
        id = 2,
        chain =
            ChainLink(
                species =
                    NamedResource(
                        name = "eevee",
                        url = "https://pokeapi.co/api/v2/pokemon-species/133/",
                    ),
                evolves_to =
                    listOf(
                        ChainLink(
                            species =
                                NamedResource(
                                    name = "vaporeon",
                                    url = "https://pokeapi.co/api/v2/pokemon-species/134/",
                                ),
                        ),
                        ChainLink(
                            species =
                                NamedResource(
                                    name = "jolteon",
                                    url = "https://pokeapi.co/api/v2/pokemon-species/135/",
                                ),
                        ),
                    ),
            ),
    )

fun bulbasaurAggregate(): PokemonDetailsAggregate =
    PokemonDetailsAggregate(
        pokemon = bulbasaurPokemonResponse(),
        species = bulbasaurSpeciesResponse(),
        evolutionChain = bulbasaurEvolutionChainResponse(),
    )

fun pokemonOnlyAggregate(): PokemonDetailsAggregate =
    PokemonDetailsAggregate(
        pokemon = bulbasaurPokemonResponse(),
        species = null,
        evolutionChain = null,
    )

private fun statSlot(
    name: String,
    baseStat: Int,
): StatSlot =
    StatSlot(
        base_stat = baseStat,
        effort = 0,
        stat = NamedResource(name, "https://pokeapi.co/api/v2/stat/1/"),
    )
