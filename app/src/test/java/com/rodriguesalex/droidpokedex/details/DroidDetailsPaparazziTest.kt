package com.rodriguesalex.droidpokedex.details

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.rodriguesalex.droidpokedex.R
import com.rodriguesalex.droidpokedex.details.viewmodel.DroidDetailsUiState
import com.rodriguesalex.droidpokedex.util.ErrorInfo
import org.junit.Rule
import org.junit.Test

class DroidDetailsPaparazziTest {
    @get:Rule
    val paparazzi =
        Paparazzi(
            deviceConfig = DeviceConfig.PIXEL_5,
            theme = "android:Theme.Material.Light.NoActionBar",
        )

    @Test
    fun details_loading() {
        paparazzi.snapshot {
            DroidDetailsScreenContent(
                topBarTitle = "Pokemon #1",
                state = DroidDetailsUiState.Loading,
                onBackClick = {},
                onRetry = {},
            )
        }
    }

    @Test
    fun details_success_bulbasaur() {
        val details = bulbasaurDetails()
        paparazzi.snapshot {
            DroidDetailsScreenContent(
                topBarTitle = detailsTopBarTitle(DroidDetailsUiState.Success(details), "1"),
                state = DroidDetailsUiState.Success(details),
                onBackClick = {},
                onRetry = {},
            )
        }
    }

    @Test
    fun details_success_offline() {
        val details = bulbasaurDetails()
        paparazzi.snapshot {
            DroidDetailsScreenContent(
                topBarTitle = detailsTopBarTitle(DroidDetailsUiState.Success(details), "1"),
                state = DroidDetailsUiState.Success(details, isOfflineData = true),
                onBackClick = {},
                onRetry = {},
            )
        }
    }

    @Test
    fun details_error_network() {
        paparazzi.snapshot {
            DroidDetailsScreenContent(
                topBarTitle = "Pokemon #1",
                state =
                    DroidDetailsUiState.Error(
                        info =
                            ErrorInfo(
                                titleRes = R.string.error_network_title,
                                messageRes = R.string.error_network_message,
                            ),
                    ),
                onBackClick = {},
                onRetry = {},
            )
        }
    }
}
