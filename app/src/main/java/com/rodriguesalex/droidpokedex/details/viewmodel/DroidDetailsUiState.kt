package com.rodriguesalex.droidpokedex.details.viewmodel

import com.rodriguesalex.details.domain.model.PokemonDetailsResponse

sealed class DroidDetailsUiState {
    object Loading : DroidDetailsUiState()

    data class Success(
        val pokemonDetails: PokemonDetailsResponse,
    ) : DroidDetailsUiState()

    object Error : DroidDetailsUiState()
}
