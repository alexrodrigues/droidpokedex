package com.rodriguesalex.details.domain.mapper

import com.rodriguesalex.details.domain.bulbasaurAggregate
import com.rodriguesalex.details.domain.bulbasaurPokemonResponse
import com.rodriguesalex.details.domain.bulbasaurSpeciesResponse
import com.rodriguesalex.details.domain.branchedEvolutionChainResponse
import com.rodriguesalex.details.domain.japaneseOnlySpeciesResponse
import com.rodriguesalex.details.domain.pokemonOnlyAggregate
import com.rodriguesalex.details.domain.model.ChainLink
import com.rodriguesalex.details.domain.model.EvolutionChainResponse
import com.rodriguesalex.details.domain.model.GenusEntry
import com.rodriguesalex.details.domain.model.NamedResource
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

    @Test
    fun `Aggregate maps branched evolution chain`() {
        val aggregate =
            pokemonOnlyAggregate().copy(
                evolutionChain = branchedEvolutionChainResponse(),
            )

        val result = aggregate.toDomain()

        assertEquals(3, result.evolutionChain.size)
        assertEquals("eevee", result.evolutionChain[0].name)
        assertEquals("vaporeon", result.evolutionChain[1].name)
        assertEquals("jolteon", result.evolutionChain[2].name)
    }

    @Test
    fun `Aggregate maps genus that already starts with uppercase letter`() {
        val aggregate =
            bulbasaurAggregate().copy(
                species =
                    bulbasaurSpeciesResponse().copy(
                        genera =
                            listOf(
                                GenusEntry(
                                    genus = "The Seed Pokémon",
                                    language =
                                        NamedResource(
                                            name = "en",
                                            url = "https://pokeapi.co/api/v2/language/9/",
                                        ),
                                ),
                            ),
                    ),
            )

        val result = aggregate.toDomain()

        assertEquals("The Seed Pokémon", result.genus)
    }

    @Test
    fun `Aggregate normalizes flavor text line breaks and form feeds`() {
        val result = bulbasaurAggregate().toDomain()

        assertEquals(
            "For some time after its birth, it grows by gaining nourishment from the seed on its back.",
            result.flavorText,
        )
    }

    @Test
    fun `PokemonDetailsResponse maps hidden abilities`() {
        val response =
            bulbasaurPokemonResponse().copy(
                abilities =
                    listOf(
                        com.rodriguesalex.details.domain.model.AbilitySlot(
                            ability = com.rodriguesalex.details.domain.model.NamedResource("chlorophyll", "url"),
                            is_hidden = true,
                            slot = 2,
                        ),
                    ),
            )

        val result = response.toDomain()

        assertEquals(1, result.abilities.size)
        assertTrue(result.abilities.first().hidden)
    }

    @Test
    fun `Aggregate uses zero id when evolution species url is invalid`() {
        val aggregate =
            pokemonOnlyAggregate().copy(
                evolutionChain =
                    EvolutionChainResponse(
                        id = 99,
                        chain =
                            ChainLink(
                                species =
                                    NamedResource(
                                        name = "unknown",
                                        url = "https://pokeapi.co/api/v2/pokemon-species/not-a-number/",
                                    ),
                            ),
                    ),
            )

        val result = aggregate.toDomain()

        assertEquals(1, result.evolutionChain.size)
        assertEquals(0, result.evolutionChain.first().id)
    }
}
