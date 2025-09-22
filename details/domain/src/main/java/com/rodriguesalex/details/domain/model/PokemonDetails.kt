package com.rodriguesalex.details.domain.model

data class PokemonDetails(
    val id: Int,
    val name: String,
    val baseExperience: Int,
    val heightMeters: Double,
    val weightKg: Double,
    val types: List<String>,
    val abilities: List<Ability>,
    val stats: Map<String, Int>,
    val sprites: Sprites
) {
    data class Ability(
        val name: String,
        val hidden: Boolean
    )

    data class Sprites(
        val frontDefault: String?,
        val backDefault: String?,
        val frontShiny: String?,
        val backShiny: String?
    )

    val primaryType: String? get() = types.firstOrNull()

    fun stat(name: String): Int? = stats[name.lowercase()]
}