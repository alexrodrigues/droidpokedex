package com.rodriguesalex.droidpokedex

import androidx.compose.material3.Text
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import org.junit.Rule
import org.junit.Test

class SimplePaparazziTest {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar"
    )

    @Test
    fun simpleText() {
        paparazzi.snapshot {
            Text("Hello Paparazzi!")
        }
    }
} 