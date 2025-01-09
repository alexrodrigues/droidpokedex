package com.rodriguesalex.droidpokedex.home.viewmodel

import com.rodriguesalex.domain.model.PokemonListItem

sealed class DroidHomeViewState {
    data object Loading : DroidHomeViewState()
    data class Success(val pokemons: List<PokemonListItem>) : DroidHomeViewState()
    data object Error : DroidHomeViewState()
}