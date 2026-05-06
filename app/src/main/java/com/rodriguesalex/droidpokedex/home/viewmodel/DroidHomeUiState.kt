package com.rodriguesalex.droidpokedex.home.viewmodel

import com.rodriguesalex.droidpokedex.util.ErrorInfo
import com.rodriguesalex.domain.model.PokemonListItem

sealed class DroidHomeUiState {
    object Loading : DroidHomeUiState()

    data class Success(
        val pokemons: List<PokemonListItem>,
        val isSearching: Boolean = false,
        val isOfflineData: Boolean = false,
    ) : DroidHomeUiState()

    data class Error(
        val info: ErrorInfo,
    ) : DroidHomeUiState()
}
