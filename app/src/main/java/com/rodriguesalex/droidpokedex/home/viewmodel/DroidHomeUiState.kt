package com.rodriguesalex.droidpokedex.home.viewmodel

import com.rodriguesalex.domain.model.PokemonListItem

sealed class DroidHomeUiState {
    object Loading : DroidHomeUiState()

    data class Success(
        val pokemons: List<PokemonListItem>,
        val isSearching: Boolean = false,
    ) : DroidHomeUiState()

    object Error : DroidHomeUiState()
}
