package com.rodriguesalex.droidpokedex.home

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.rodriguesalex.droidpokedex.R
import com.rodriguesalex.droidpokedex.home.viewmodel.DroidHomeUiState
import com.rodriguesalex.droidpokedex.util.ErrorInfo
import org.junit.Rule
import org.junit.Test

class DroidHomePaparazziTest {
    @get:Rule
    val paparazzi =
        Paparazzi(
            deviceConfig = DeviceConfig.PIXEL_5,
            theme = "android:Theme.Material.Light.NoActionBar",
        )

    @Test
    fun home_success_empty_list() {
        paparazzi.snapshot {
            DroidHomeScreenContent(
                state = DroidHomeUiState.Success(pokemons = emptyList()),
                searchQuery = "",
                isLoading = false,
                onSearchQueryChanged = {},
                onPokemonClick = {},
                onRetry = {},
                onLoadMore = {},
            )
        }
    }

    @Test
    fun home_loading() {
        paparazzi.snapshot {
            DroidHomeScreenContent(
                state = DroidHomeUiState.Loading,
                searchQuery = "",
                isLoading = false,
                onSearchQueryChanged = {},
                onPokemonClick = {},
                onRetry = {},
                onLoadMore = {},
            )
        }
    }

    @Test
    fun home_success() {
        paparazzi.snapshot {
            DroidHomeScreenContent(
                state =
                    DroidHomeUiState.Success(
                        pokemons = homePokemonFixtures(),
                    ),
                searchQuery = "",
                isLoading = false,
                onSearchQueryChanged = {},
                onPokemonClick = {},
                onRetry = {},
                onLoadMore = {},
            )
        }
    }

    @Test
    fun home_success_offline() {
        paparazzi.snapshot {
            DroidHomeScreenContent(
                state =
                    DroidHomeUiState.Success(
                        pokemons = homePokemonFixtures(),
                        isOfflineData = true,
                    ),
                searchQuery = "",
                isLoading = false,
                onSearchQueryChanged = {},
                onPokemonClick = {},
                onRetry = {},
                onLoadMore = {},
            )
        }
    }

    @Test
    fun home_success_pagination_loading() {
        paparazzi.snapshot {
            DroidHomeScreenContent(
                state =
                    DroidHomeUiState.Success(
                        pokemons = homePokemonFixtures(),
                    ),
                searchQuery = "",
                isLoading = true,
                onSearchQueryChanged = {},
                onPokemonClick = {},
                onRetry = {},
                onLoadMore = {},
            )
        }
    }

    @Test
    fun home_success_searching() {
        paparazzi.snapshot {
            DroidHomeScreenContent(
                state =
                    DroidHomeUiState.Success(
                        pokemons = listOf(homePokemonFixtures().first()),
                        isSearching = true,
                    ),
                searchQuery = "bulbasaur",
                isLoading = false,
                onSearchQueryChanged = {},
                onPokemonClick = {},
                onRetry = {},
                onLoadMore = {},
            )
        }
    }

    @Test
    fun home_error_server() {
        paparazzi.snapshot {
            DroidHomeScreenContent(
                state =
                    DroidHomeUiState.Error(
                        info =
                            ErrorInfo(
                                titleRes = R.string.error_server_title,
                                messageRes = R.string.error_server_message,
                                detailRes = R.string.error_server_detail,
                                detailArg = 503,
                            ),
                    ),
                searchQuery = "",
                isLoading = false,
                onSearchQueryChanged = {},
                onPokemonClick = {},
                onRetry = {},
                onLoadMore = {},
            )
        }
    }

    @Test
    fun home_search_bar_with_query() {
        paparazzi.snapshot {
            DroidHomeScreenContent(
                state =
                    DroidHomeUiState.Success(
                        pokemons = homePokemonFixtures(),
                    ),
                searchQuery = "char",
                isLoading = false,
                onSearchQueryChanged = {},
                onPokemonClick = {},
                onRetry = {},
                onLoadMore = {},
            )
        }
    }

    @Test
    fun home_error_invalid_id_detail() {
        paparazzi.snapshot {
            DroidHomeScreenContent(
                state =
                    DroidHomeUiState.Error(
                        info =
                            ErrorInfo(
                                titleRes = R.string.error_invalid_id_title,
                                messageRes = R.string.error_invalid_id_message,
                                detailRes = R.string.error_invalid_id_message,
                            ),
                    ),
                searchQuery = "",
                isLoading = false,
                onSearchQueryChanged = {},
                onPokemonClick = {},
                onRetry = {},
                onLoadMore = {},
            )
        }
    }

    @Test
    fun home_error_network() {
        paparazzi.snapshot {
            DroidHomeScreenContent(
                state =
                    DroidHomeUiState.Error(
                        info =
                            ErrorInfo(
                                titleRes = R.string.error_network_title,
                                messageRes = R.string.error_network_message,
                            ),
                    ),
                searchQuery = "",
                isLoading = false,
                onSearchQueryChanged = {},
                onPokemonClick = {},
                onRetry = {},
                onLoadMore = {},
            )
        }
    }
}
