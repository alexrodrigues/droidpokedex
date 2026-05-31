package com.rodriguesalex.domain.mapper

import com.rodriguesalex.domain.bulbasaurDetailedResponse
import com.rodriguesalex.domain.model.DroidPokemonTypeColor
import com.rodriguesalex.domain.pokemonListResponseWithDetails
import com.rodriguesalex.domain.pokemonListResponseWithoutDetails
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class DroidHomeMapperTest {
    @Test
    fun `PokemonListResponse toModel maps empty results when detailedResults is null`() {
        val result = pokemonListResponseWithoutDetails().toModel()

        assertEquals(1, result.count)
        assertTrue(result.results.isEmpty())
    }

    @Test
    fun `PokemonListResponse toModel maps detailed results`() {
        val result = pokemonListResponseWithDetails().toModel()

        assertEquals(1, result.results.size)
        assertEquals("bulbasaur", result.results.first().name)
    }

    @Test
    fun `PokemonListDetailedItemResponse toModel maps core fields and artwork url`() {
        val result = bulbasaurDetailedResponse().toModel()

        assertEquals(1, result.id)
        assertEquals("bulbasaur", result.name)
        assertEquals(64, result.baseExperience)
        assertTrue(result.pokemonImageUrl.endsWith("/1.png"))
        assertEquals(DroidPokemonTypeColor.GRASS.primary, result.backgroundColor)
        assertEquals(listOf("grass", "poison"), result.types.map { it.type.name })
        assertEquals(45, result.stats.first().baseStat)
    }

    @Test
    fun `SpritesResponse toModel maps nullable sprite urls`() {
        val sprites = bulbasaurDetailedResponse().sprites

        val result = sprites.toModel()

        assertEquals("https://pokeapi.co/sprites/pokemon/1.png", result.frontDefault)
    }

    @Test
    fun `TypeSlotResponse toModel maps slot and type name`() {
        val typeSlot = bulbasaurDetailedResponse().types.first()

        val result = typeSlot.toModel()

        assertEquals(1, result.slot)
        assertEquals("grass", result.type.name)
    }

    @Test
    fun `PokemonListDetailedItemResponse toModel uses normal color when types empty`() {
        val response = bulbasaurDetailedResponse().copy(types = emptyList())

        val result = response.toModel()

        assertEquals(DroidPokemonTypeColor.NORMAL.primary, result.backgroundColor)
    }

    @Test
    fun `AbilitySlotResponse toModel maps hidden flag and slot`() {
        val ability = bulbasaurDetailedResponse().abilities.first()

        val result = ability.toModel()

        assertEquals(false, result.isHidden)
        assertEquals(1, result.slot)
        assertEquals("overgrow", result.ability.name)
    }

    @Test
    fun `NamedAPIResourceResponse toModel maps name and url`() {
        val resource = bulbasaurDetailedResponse().species

        val result = resource.toModel()

        assertEquals("bulbasaur", result.name)
        assertTrue(result.url.contains("pokemon-species"))
    }

    @Test
    fun `StatSlotResponse toModel maps stat effort and base stat`() {
        val stat = bulbasaurDetailedResponse().stats.first()

        val result = stat.toModel()

        assertEquals("hp", result.stat.name)
        assertEquals(0, result.effort)
        assertEquals(45, result.baseStat)
    }

    @Test
    fun `GameIndexResponse toModel maps index and version`() {
        val gameIndex = bulbasaurDetailedResponse().game_indices.first()

        val result = gameIndex.toModel()

        assertEquals(153, result.gameIndex)
        assertEquals("red", result.version.name)
    }

    @Test
    fun `HeldItemResponse toModel maps item and version details`() {
        val heldItem = bulbasaurDetailedResponse().held_items.first()

        val result = heldItem.toModel()

        assertEquals("miracle-seed", result.item.name)
        assertEquals(5, result.versionDetails.first().rarity)
    }

    @Test
    fun `PokemonListDetailedItemResponse toModel maps nested collections`() {
        val result = bulbasaurDetailedResponse().toModel()

        assertEquals(1, result.abilities.size)
        assertEquals(1, result.gameIndices.size)
        assertEquals(1, result.heldItems.size)
        assertEquals(1, result.moves.size)
        assertEquals(2, result.types.size)
    }

    @Test
    fun `HeldItemVersionResponse toModel maps version and rarity`() {
        val version = bulbasaurDetailedResponse().held_items.first().version_details.first()

        val result = version.toModel()

        assertEquals("ruby", result.version.name)
        assertEquals(5, result.rarity)
    }

    @Test
    fun `MoveVersionResponse toModel maps learn method and level`() {
        val moveVersion = bulbasaurDetailedResponse().moves.first().version_group_details.first()

        val result = moveVersion.toModel()

        assertEquals("level-up", result.moveLearnMethod.name)
        assertEquals(1, result.levelLearnedAt)
    }
}
