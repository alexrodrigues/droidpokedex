package com.rodriguesalex.droidpokedex.details.viewmodel

import com.rodriguesalex.details.domain.model.PokemonDetails


sealed class DroidDetailsUiState {
    object Loading : DroidDetailsUiState()

    data class Success(
        val pokemonDetails: PokemonDetails,
    ) : DroidDetailsUiState()

    object Error : DroidDetailsUiState()
}
