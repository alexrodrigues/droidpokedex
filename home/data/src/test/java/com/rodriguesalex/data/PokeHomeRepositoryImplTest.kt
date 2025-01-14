package com.rodriguesalex.data

import com.rodriguesalex.data.repository.PokeHomeRepositoryImpl
import com.rodriguesalex.data.service.PokeHomeService
import com.rodriguesalex.domain.model.PokemonListDetailedItemResponse
import com.rodriguesalex.domain.model.PokemonListItemResponse
import com.rodriguesalex.domain.model.PokemonListResponse
import com.rodriguesalex.domain.repository.PokeHomeRepository
import io.mockk.MockKAnnotations
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.testng.annotations.BeforeTest


class PokeHomeRepositoryImplTest {

    @MockK
    private lateinit var pokeHomeService: PokeHomeService

    private lateinit var pokeHomeRepository: PokeHomeRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        pokeHomeRepository = PokeHomeRepositoryImpl(pokeHomeService)
    }

    @Test
    fun `fetchPokemonHome should fetch and enrich pokemon list`() = runTest {
        // Arrange
        val limit = 10
        val offset = 0

        val pikachuDetails = mockk<PokemonListDetailedItemResponse> {
            every { name } returns "Pikachu"
        }

        val bulbasaurDetails = mockk<PokemonListDetailedItemResponse> {
            every { name } returns "Bulbasaur"
        }

        val pokemonResults = listOf(
            PokemonListItemResponse(name = "Pikachu", url = "https://pokeapi.co/api/v2/pokemon/1/"),
            PokemonListItemResponse(
                name = "Bulbasaur",
                url = "https://pokeapi.co/api/v2/pokemon/2/"
            )
        )
        val pokemonListResponse = PokemonListResponse(
            results = pokemonResults,
            count = 10,
            next = "https://pokeapi.co/api/v2/pokemon?offset=10&limit=10",
            previous = null
        )


        coEvery { pokeHomeService.fetchPokemonList(limit, offset) } returns pokemonListResponse
        coEvery {
            pokeHomeService.fetchHomePokemonDetail("https://pokeapi.co/api/v2/pokemon/1/")
        } returns pikachuDetails

        coEvery {
            pokeHomeService.fetchHomePokemonDetail("https://pokeapi.co/api/v2/pokemon/2/")
        } returns bulbasaurDetails

        // Act
        val result = pokeHomeRepository.fetchPokemonHome(limit, offset)

        // Assert
        assertEquals(2, result.detailedResults!!.size)
        assertEquals(pikachuDetails.name, result.detailedResults!!.first().name)
        assertEquals(bulbasaurDetails.name, result.detailedResults!![1].name)
    }
}