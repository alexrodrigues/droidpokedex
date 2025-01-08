package com.rodriguesalex.domain.mapper

import androidx.compose.ui.graphics.Color
import com.rodriguesalex.domain.model.PokemonList
import com.rodriguesalex.domain.model.PokemonListItem
import com.rodriguesalex.domain.model.PokemonListItemResponse
import com.rodriguesalex.domain.model.PokemonListResponse


fun PokemonListResponse.toModel(): PokemonList =
    PokemonList(
        count = count,
        next = next,
        previous = previous,
        results = results.map { it.toModel() }
    )


fun PokemonListItemResponse.toModel(): PokemonListItem {
    val id = extractIdFromUrl(url).orEmpty()
    return PokemonListItem(
        id = id,
        name = name,
        url = url,
        pokemonImageUrl = "https://raw.githubusercontent.com" +
                "/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${id}.png",
        backgroundColor = Color(0xFF63B556),
    )
}

private fun extractIdFromUrl(url: String): String? {
    val regex = ".*/(\\d+)/?$".toRegex()
    val matchResult = regex.find(url)
    return matchResult?.groupValues?.get(1)
}
