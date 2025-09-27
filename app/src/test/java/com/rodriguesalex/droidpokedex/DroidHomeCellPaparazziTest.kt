package com.rodriguesalex.droidpokedex

import androidx.compose.ui.graphics.Color
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.rodriguesalex.droidpokedex.designsystem.components.DroidHomeCell
import org.junit.Rule
import org.junit.Test

class DroidHomeCellPaparazziTest {
    @get:Rule
    val paparazzi =
        Paparazzi(
            deviceConfig = DeviceConfig.PIXEL_5,
            theme = "android:Theme.Material.Light.NoActionBar",
        )

    @Test
    fun `DroidHomeCell with grass type pokemon`() {
        paparazzi.snapshot {
            DroidHomeCell(
                pokemonName = "bulbasaur",
                pokemonNumber = 1,
                pokemonImageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                types = listOf("grass", "poison"),
                backgroundColor = Color(0xFF63B556),
                pokeballImageRes = R.drawable.pokeball,
            )
        }
    }

    @Test
    fun `DroidHomeCell with fire type pokemon`() {
        paparazzi.snapshot {
            DroidHomeCell(
                pokemonName = "charmander",
                pokemonNumber = 4,
                pokemonImageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/4.png",
                types = listOf("fire"),
                backgroundColor = Color(0xFFF08030),
                pokeballImageRes = R.drawable.pokeball,
            )
        }
    }

    @Test
    fun `DroidHomeCell with water type pokemon`() {
        paparazzi.snapshot {
            DroidHomeCell(
                pokemonName = "squirtle",
                pokemonNumber = 7,
                pokemonImageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/7.png",
                types = listOf("water"),
                backgroundColor = Color(0xFF6890F0),
                pokeballImageRes = R.drawable.pokeball,
            )
        }
    }
}
