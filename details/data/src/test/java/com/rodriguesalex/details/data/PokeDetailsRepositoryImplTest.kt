package com.rodriguesalex.details.data

import com.rodriguesalex.details.data.repository.PokeDetailsRepositoryImpl
import com.rodriguesalex.details.data.service.PokeDetailsService
import com.rodriguesalex.details.domain.model.DetailsRemoteSource
import com.rodriguesalex.details.domain.repository.PokeDetailsRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import java.io.IOException
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PokeDetailsRepositoryImplTest {
    @MockK
    private lateinit var pokeDetailsService: PokeDetailsService

    private lateinit var repository: PokeDetailsRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repository = PokeDetailsRepositoryImpl(pokeDetailsService)
    }

    @Test
    fun `fetchPokemonDetails fetches pokemon species and evolution`() =
        runTest {
            val pokemon = testPokemonResponse()
            val species = testSpeciesResponse()
            val evolution = testEvolutionChainResponse()

            coEvery { pokeDetailsService.fetchPokemonDetails(1) } returns pokemon
            coEvery { pokeDetailsService.fetchPokemonSpecies(1) } returns species
            coEvery { pokeDetailsService.fetchEvolutionChain(species.evolution_chain!!.url) } returns evolution

            val result = repository.fetchPokemonDetails(1)

            assertEquals(DetailsRemoteSource.NETWORK, result.source)
            assertEquals(pokemon, result.value.pokemon)
            assertEquals(species, result.value.species)
            assertEquals(evolution, result.value.evolutionChain)
            coVerify { pokeDetailsService.fetchPokemonDetails(1) }
            coVerify { pokeDetailsService.fetchPokemonSpecies(1) }
            coVerify { pokeDetailsService.fetchEvolutionChain("https://pokeapi.co/api/v2/evolution-chain/1/") }
        }

    @Test
    fun `fetchPokemonDetails omits evolution when species has no chain url`() =
        runTest {
            val pokemon = testPokemonResponse()
            val species = testSpeciesResponseWithoutEvolution()

            coEvery { pokeDetailsService.fetchPokemonDetails(1) } returns pokemon
            coEvery { pokeDetailsService.fetchPokemonSpecies(1) } returns species

            val result = repository.fetchPokemonDetails(1)

            assertNull(result.value.evolutionChain)
            coVerify(exactly = 0) { pokeDetailsService.fetchEvolutionChain(any()) }
        }

    @Test
    fun `fetchPokemonDetails succeeds when species fetch fails`() =
        runTest {
            val pokemon = testPokemonResponse()

            coEvery { pokeDetailsService.fetchPokemonDetails(1) } returns pokemon
            coEvery { pokeDetailsService.fetchPokemonSpecies(1) } throws IOException("species down")

            val result = repository.fetchPokemonDetails(1)

            assertEquals(DetailsRemoteSource.NETWORK, result.source)
            assertEquals(pokemon, result.value.pokemon)
            assertNull(result.value.species)
            assertNull(result.value.evolutionChain)
        }

    @Test
    fun `fetchPokemonDetails succeeds when evolution fetch fails`() =
        runTest {
            val pokemon = testPokemonResponse()
            val species = testSpeciesResponse()

            coEvery { pokeDetailsService.fetchPokemonDetails(1) } returns pokemon
            coEvery { pokeDetailsService.fetchPokemonSpecies(1) } returns species
            coEvery { pokeDetailsService.fetchEvolutionChain(any()) } throws IOException("evolution down")

            val result = repository.fetchPokemonDetails(1)

            assertEquals(DetailsRemoteSource.NETWORK, result.source)
            assertEquals(pokemon, result.value.pokemon)
            assertEquals(species, result.value.species)
            assertNull(result.value.evolutionChain)
        }

    @Test
    fun `fetchPokemonDetails returns cache after IOException`() =
        runTest {
            val pokemon = testPokemonResponse()
            val species = testSpeciesResponse()
            val evolution = testEvolutionChainResponse()

            coEvery { pokeDetailsService.fetchPokemonDetails(1) } returns pokemon
            coEvery { pokeDetailsService.fetchPokemonSpecies(1) } returns species
            coEvery { pokeDetailsService.fetchEvolutionChain(any()) } returns evolution

            val first = repository.fetchPokemonDetails(1)
            assertEquals(DetailsRemoteSource.NETWORK, first.source)

            var fetchCount = 0
            coEvery { pokeDetailsService.fetchPokemonDetails(1) } answers {
                fetchCount++
                throw IOException("offline")
            }

            val second = repository.fetchPokemonDetails(1)
            assertEquals(DetailsRemoteSource.CACHE, second.source)
            assertEquals(first.value.pokemon.name, second.value.pokemon.name)
            assertNotNull(second.value.species)
        }

    @Test
    fun `fetchPokemonDetails throws when no cache on failure`() =
        runTest {
            coEvery { pokeDetailsService.fetchPokemonDetails(99) } throws IOException("offline")

            try {
                repository.fetchPokemonDetails(99)
                error("Expected IOException")
            } catch (e: IOException) {
                assertEquals("offline", e.message)
            }
        }
}
