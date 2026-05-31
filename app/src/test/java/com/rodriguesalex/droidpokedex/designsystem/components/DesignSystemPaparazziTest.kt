package com.rodriguesalex.droidpokedex.designsystem.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.rodriguesalex.details.domain.model.PokemonDetails
import com.rodriguesalex.domain.model.DroidPokemonTypeColor
import com.rodriguesalex.droidpokedex.details.bulbasaurDetails
import org.junit.Rule
import org.junit.Test

class DesignSystemPaparazziTest {
    @get:Rule
    val paparazzi =
        Paparazzi(
            deviceConfig = DeviceConfig.PIXEL_5,
            theme = "android:Theme.Material.Light.NoActionBar",
        )

    @Test
    fun droid_error_component_without_detail() {
        paparazzi.snapshot {
            DroidErrorComponent(
                title = "Network error",
                message = "Check your connection and try again.",
                onRetryClick = {},
            )
        }
    }

    @Test
    fun droid_error_component_with_detail() {
        paparazzi.snapshot {
            DroidErrorComponent(
                title = "Server error",
                message = "The server returned an error.",
                detail = "HTTP 503",
                onRetryClick = {},
            )
        }
    }

    @Test
    fun base_stats_section_bulbasaur() {
        paparazzi.snapshot {
            BaseStatsSection(
                stats =
                    mapOf(
                        "hp" to 45,
                        "attack" to 49,
                        "defense" to 49,
                        "special-attack" to 65,
                        "special-defense" to 65,
                        "speed" to 45,
                    ),
            )
        }
    }

    @Test
    fun evolution_chain_section() {
        paparazzi.snapshot {
            EvolutionChainSection(
                stages =
                    listOf(
                        evolutionStage(1, "bulbasaur"),
                        evolutionStage(2, "ivysaur"),
                        evolutionStage(3, "venusaur"),
                    ),
                currentPokemonId = 1,
                primaryType = "grass",
            )
        }
    }

    @Test
    fun base_stats_section_empty() {
        paparazzi.snapshot {
            BaseStatsSection(stats = emptyMap())
        }
    }

    @Test
    fun base_stats_section_partial() {
        paparazzi.snapshot {
            BaseStatsSection(
                stats =
                    mapOf(
                        "hp" to 45,
                        "attack" to 49,
                    ),
            )
        }
    }

    @Test
    fun evolution_chain_section_current_ivysaur() {
        paparazzi.snapshot {
            EvolutionChainSection(
                stages =
                    listOf(
                        evolutionStage(1, "bulbasaur"),
                        evolutionStage(2, "ivysaur"),
                        evolutionStage(3, "venusaur"),
                    ),
                currentPokemonId = 2,
                primaryType = "grass",
            )
        }
    }

    @Test
    fun droid_primary_button() {
        paparazzi.snapshot {
            DroidPrimaryButton(text = "Retry", onClick = {})
        }
    }

    @Test
    fun droid_secondary_button() {
        paparazzi.snapshot {
            DroidSecondaryButton(text = "Cancel", onClick = {})
        }
    }

    @Test
    fun droid_pagination_loading_indicator() {
        paparazzi.snapshot {
            DroidPaginationLoadingIndicator(modifier = Modifier)
        }
    }

    @Test
    fun details_top_bar() {
        paparazzi.snapshot {
            DetailsTopBar(
                title = "Bulbasaur Entry #001",
                onBackClick = {},
            )
        }
    }

    @Test
    fun pokemon_hero_section() {
        val details = bulbasaurDetails()
        paparazzi.snapshot {
            PokemonHeroSection(
                pokemonId = details.id,
                imageUrl = details.officialArtworkUrl,
                backgroundColor = Color(0xFF62B957),
            )
        }
    }

    @Test
    fun description_card() {
        paparazzi.snapshot {
            DescriptionCard(
                description =
                    "For some time after its birth, it grows by gaining nourishment from the seed on its back.",
            )
        }
    }

    @Test
    fun stat_info_card() {
        paparazzi.snapshot {
            StatInfoCard(
                label = "Height",
                value = "0.7 m",
                icon = Icons.Outlined.Straighten,
                iconTint = Color(0xFF62B957),
            )
        }
    }

    @Test
    fun type_pill_tags_all_types() {
        DroidPokemonTypeColor.entries.forEach { typeColor ->
            paparazzi.snapshot(name = "type_pill_${typeColor.name.lowercase()}") {
                TypePillTag(type = typeColor.name.lowercase())
            }
        }
    }

    @Test
    fun evolution_chain_empty() {
        paparazzi.snapshot {
            EvolutionChainSection(
                stages = emptyList(),
                currentPokemonId = 1,
                primaryType = "grass",
            )
        }
    }

    @Test
    fun droid_poke_header() {
        paparazzi.snapshot {
            DroidPokeHeader(modifier = Modifier)
        }
    }

    private fun evolutionStage(
        id: Int,
        name: String,
    ): PokemonDetails.EvolutionStage =
        PokemonDetails.EvolutionStage(
            id = id,
            name = name,
            spriteUrl =
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png",
        )
}
