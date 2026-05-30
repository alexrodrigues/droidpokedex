package com.rodriguesalex.droidpokedex.details

import com.rodriguesalex.details.domain.model.PokemonDetails
import com.rodriguesalex.droidpokedex.details.viewmodel.DroidDetailsUiState
import junit.framework.TestCase.assertEquals
import org.junit.Test

class DetailsTopBarTitleTest {
    @Test
    fun `detailsTopBarTitle formats success state with padded id`() {
        val details =
            PokemonDetails(
                id = 1,
                name = "bulbasaur",
                baseExperience = 64,
                heightMeters = 0.7,
                weightKg = 6.9,
                types = listOf("grass"),
                abilities = emptyList(),
                stats = emptyMap(),
                sprites =
                    PokemonDetails.Sprites(
                        frontDefault = null,
                        backDefault = null,
                        frontShiny = null,
                        backShiny = null,
                    ),
                officialArtworkUrl = "https://example.com/1.png",
                genus = null,
                flavorText = null,
                evolutionChain = emptyList(),
            )

        val title =
            detailsTopBarTitle(
                DroidDetailsUiState.Success(details),
                pokemonId = "1",
            )

        assertEquals("Bulbasaur Entry #001", title)
    }

    @Test
    fun `detailsTopBarTitle keeps already capitalized names`() {
        val details =
            PokemonDetails(
                id = 25,
                name = "Pikachu",
                baseExperience = 112,
                heightMeters = 0.4,
                weightKg = 6.0,
                types = listOf("electric"),
                abilities = emptyList(),
                stats = emptyMap(),
                sprites =
                    PokemonDetails.Sprites(
                        frontDefault = null,
                        backDefault = null,
                        frontShiny = null,
                        backShiny = null,
                    ),
                officialArtworkUrl = "https://example.com/25.png",
                genus = null,
                flavorText = null,
                evolutionChain = emptyList(),
            )

        val title =
            detailsTopBarTitle(
                DroidDetailsUiState.Success(details),
                pokemonId = "25",
            )

        assertEquals("Pikachu Entry #025", title)
    }

    @Test
    fun `detailsTopBarTitle uses fallback for loading state`() {
        assertEquals(
            "Pokemon #42",
            detailsTopBarTitle(DroidDetailsUiState.Loading, pokemonId = "42"),
        )
    }

    @Test
    fun `detailsTopBarTitle uses fallback for error state`() {
        assertEquals(
            "Pokemon #7",
            detailsTopBarTitle(
                DroidDetailsUiState.Error(
                    com.rodriguesalex.droidpokedex.util.ErrorInfo(
                        titleRes = com.rodriguesalex.droidpokedex.R.string.error_network_title,
                        messageRes = com.rodriguesalex.droidpokedex.R.string.error_network_message,
                    ),
                ),
                pokemonId = "7",
            ),
        )
    }
}
