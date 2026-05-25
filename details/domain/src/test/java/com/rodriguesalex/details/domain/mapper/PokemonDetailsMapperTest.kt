package com.rodriguesalex.details.domain.mapper

import com.rodriguesalex.details.domain.bulbasaurAggregate
import com.rodriguesalex.details.domain.bulbasaurPokemonResponse
import com.rodriguesalex.details.domain.japaneseOnlySpeciesResponse
import com.rodriguesalex.details.domain.pokemonOnlyAggregate
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Test

class PokemonDetailsMapperTest {
    @Test
    fun `PokemonDetailsResponse maps core fields`() {
        val result = bulbasaurPokemonResponse().toDomain()

        assertEquals(1, result.id)
        assertEquals("bulbasaur", result.name)
        assertEquals(64, result.baseExperience)
        assertEquals(0.7, result.heightMeters, 0.001)
        assertEquals(6.9, result.weightKg, 0.001)
        assertEquals(listOf("grass", "poison"), result.types)
        assertEquals(45, result.stats["hp"])
        assertEquals(49, result.stats["attack"])
        assertEquals(65, result.stats["special-attack"])
        assertTrue(result.officialArtworkUrl.endsWith("/1.png"))
    }

    @Test
    fun `Aggregate maps genus and flavor from English entries`() {
        val result = bulbasaurAggregate().toDomain()

        assertEquals("The Seed Pokémon", result.genus)
        assertEquals(
            "For some time after its birth, it grows by gaining nourishment from the seed on its back.",
            result.flavorText,
        )
    }

    @Test
    fun `Aggregate ignores non-English genus and flavor`() {
        val aggregate =
            pokemonOnlyAggregate().copy(species = japaneseOnlySpeciesResponse())
        val result = aggregate.toDomain()

        assertNull(result.genus)
        assertNull(result.flavorText)
    }

    @Test
    fun `Aggregate flattens linear evolution chain`() {
        val result = bulbasaurAggregate().toDomain()

        assertEquals(3, result.evolutionChain.size)
        assertEquals(1, result.evolutionChain[0].id)
        assertEquals("bulbasaur", result.evolutionChain[0].name)
        assertTrue(result.evolutionChain[0].spriteUrl.endsWith("/1.png"))
        assertEquals(2, result.evolutionChain[1].id)
        assertEquals("ivysaur", result.evolutionChain[1].name)
        assertEquals(3, result.evolutionChain[2].id)
        assertEquals("venusaur", result.evolutionChain[2].name)
    }

    @Test
    fun `Aggregate with null species and evolution`() {
        val result = pokemonOnlyAggregate().toDomain()

        assertNull(result.genus)
        assertNull(result.flavorText)
        assertTrue(result.evolutionChain.isEmpty())
    }
}
