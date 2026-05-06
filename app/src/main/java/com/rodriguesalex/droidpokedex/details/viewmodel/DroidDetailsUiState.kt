package com.rodriguesalex.droidpokedex.details.viewmodel

import com.rodriguesalex.details.domain.model.PokemonDetails
import com.rodriguesalex.droidpokedex.util.ErrorInfo

sealed class DroidDetailsUiState {
    object Loading : DroidDetailsUiState()

    data class Success(
        val pokemonDetails: PokemonDetails,
        val isOfflineData: Boolean = false,
    ) : DroidDetailsUiState()

    data class Error(
        val info: ErrorInfo,
    ) : DroidDetailsUiState()
}
