package com.rodriguesalex.domain.model

import androidx.compose.ui.graphics.Color

data class PokemonListItem (
    val id: String,
    val name: String,
    val url: String,
    val pokemonImageUrl: String,
    val backgroundColor: Color,
)