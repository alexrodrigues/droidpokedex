package com.rodriguesalex.droidpokedex.home

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import org.junit.Rule
import org.junit.Test

class PokeSearchBarPaparazziTest {
    @get:Rule
    val paparazzi =
        Paparazzi(
            deviceConfig = DeviceConfig.PIXEL_5,
            theme = "android:Theme.Material.Light.NoActionBar",
        )

    @Test
    fun poke_search_bar_empty() {
        paparazzi.snapshot {
            PokeSearchBar(
                searchQuery = "",
                onSearchQueryChanged = {},
            )
        }
    }

    @Test
    fun poke_search_bar_with_query() {
        paparazzi.snapshot {
            PokeSearchBar(
                searchQuery = "pikachu",
                onSearchQueryChanged = {},
            )
        }
    }
}
