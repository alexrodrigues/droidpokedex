package com.rodriguesalex.domain

import com.rodriguesalex.domain.model.AbilitySlotResponse
import com.rodriguesalex.domain.model.GameIndexResponse
import com.rodriguesalex.domain.model.HeldItemResponse
import com.rodriguesalex.domain.model.HeldItemVersionResponse
import com.rodriguesalex.domain.model.MoveSlotResponse
import com.rodriguesalex.domain.model.MoveVersionResponse
import com.rodriguesalex.domain.model.NamedAPIResourceResponse
import com.rodriguesalex.domain.model.PokemonListDetailedItemResponse
import com.rodriguesalex.domain.model.PokemonListItemResponse
import com.rodriguesalex.domain.model.PokemonListResponse
import com.rodriguesalex.domain.model.SpritesResponse
import com.rodriguesalex.domain.model.StatSlotResponse
import com.rodriguesalex.domain.model.TypeSlotResponse

fun bulbasaurListItemResponse(): PokemonListItemResponse =
    PokemonListItemResponse(
        name = "bulbasaur",
        url = "https://pokeapi.co/api/v2/pokemon/1/",
    )

fun bulbasaurDetailedResponse(): PokemonListDetailedItemResponse =
    PokemonListDetailedItemResponse(
        id = 1,
        name = "bulbasaur",
        base_experience = 64,
        height = 7,
        is_default = true,
        order = 1,
        weight = 69,
        abilities =
            listOf(
                AbilitySlotResponse(
                    is_hidden = false,
                    slot = 1,
                    ability = NamedAPIResourceResponse("overgrow", "https://pokeapi.co/api/v2/ability/65/"),
                ),
            ),
        forms = emptyList(),
        game_indices =
            listOf(
                GameIndexResponse(
                    game_index = 153,
                    version = NamedAPIResourceResponse("red", "https://pokeapi.co/api/v2/version/1/"),
                ),
            ),
        held_items =
            listOf(
                HeldItemResponse(
                    item = NamedAPIResourceResponse("miracle-seed", "https://pokeapi.co/api/v2/item/45/"),
                    version_details =
                        listOf(
                            HeldItemVersionResponse(
                                version = NamedAPIResourceResponse("ruby", "https://pokeapi.co/api/v2/version/7/"),
                                rarity = 5,
                            ),
                        ),
                ),
            ),
        location_area_encounters = "https://pokeapi.co/api/v2/pokemon/1/encounters",
        moves =
            listOf(
                MoveSlotResponse(
                    move = NamedAPIResourceResponse("tackle", "https://pokeapi.co/api/v2/move/33/"),
                    version_group_details =
                        listOf(
                            MoveVersionResponse(
                                move_learn_method =
                                    NamedAPIResourceResponse("level-up", "https://pokeapi.co/api/v2/move-learn-method/1/"),
                                version_group =
                                    NamedAPIResourceResponse("red-blue", "https://pokeapi.co/api/v2/version-group/1/"),
                                level_learned_at = 1,
                            ),
                        ),
                ),
            ),
        species = NamedAPIResourceResponse("bulbasaur", "https://pokeapi.co/api/v2/pokemon-species/1/"),
        sprites =
            SpritesResponse(
                back_default = null,
                back_female = null,
                back_shiny = null,
                back_shiny_female = null,
                front_default = "https://pokeapi.co/sprites/pokemon/1.png",
                front_female = null,
                front_shiny = null,
                front_shiny_female = null,
            ),
        stats =
            listOf(
                StatSlotResponse(
                    stat = NamedAPIResourceResponse("hp", "https://pokeapi.co/api/v2/stat/1/"),
                    effort = 0,
                    base_stat = 45,
                ),
            ),
        types =
            listOf(
                TypeSlotResponse(
                    slot = 1,
                    type = NamedAPIResourceResponse("grass", "https://pokeapi.co/api/v2/type/12/"),
                ),
                TypeSlotResponse(
                    slot = 2,
                    type = NamedAPIResourceResponse("poison", "https://pokeapi.co/api/v2/type/4/"),
                ),
            ),
    )

fun pokemonListResponseWithoutDetails(): PokemonListResponse =
    PokemonListResponse(
        count = 1,
        next = null,
        previous = null,
        results = listOf(bulbasaurListItemResponse()),
    )

fun pokemonListResponseWithDetails(): PokemonListResponse =
    pokemonListResponseWithoutDetails().copy(
        detailedResults = listOf(bulbasaurDetailedResponse()),
    )
