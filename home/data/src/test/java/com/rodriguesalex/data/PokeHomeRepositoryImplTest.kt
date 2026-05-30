package com.rodriguesalex.data

import com.rodriguesalex.data.repository.PokeHomeRepositoryImpl
import com.rodriguesalex.data.service.PokeHomeService
import com.rodriguesalex.domain.model.PokemonListDetailedItemResponse
import com.rodriguesalex.domain.model.PokemonListItemResponse
import com.rodriguesalex.domain.model.PokemonListResponse
import com.rodriguesalex.domain.model.RemoteDataSource
import com.rodriguesalex.domain.repository.PokeHomeRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import java.io.IOException
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.fail
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException

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
    fun `fetchPokemonHome should fetch and enrich pokemon list`() =
        runTest {
            // Arrange
            val limit = 10
            val offset = 0

            val pikachuDetails =
                mockk<PokemonListDetailedItemResponse> {
                    every { name } returns "Pikachu"
                }

            val bulbasaurDetails =
                mockk<PokemonListDetailedItemResponse> {
                    every { name } returns "Bulbasaur"
                }

            val pokemonResults =
                listOf(
                    PokemonListItemResponse(name = "Pikachu", url = "https://pokeapi.co/api/v2/pokemon/1/"),
                    PokemonListItemResponse(
                        name = "Bulbasaur",
                        url = "https://pokeapi.co/api/v2/pokemon/2/",
                    ),
                )
            val pokemonListResponse =
                PokemonListResponse(
                    results = pokemonResults,
                    count = 10,
                    next = "https://pokeapi.co/api/v2/pokemon?offset=10&limit=10",
                    previous = null,
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
            assertEquals(RemoteDataSource.NETWORK, result.source)
            assertEquals(2, result.value.detailedResults!!.size)
            assertEquals(pikachuDetails.name, result.value.detailedResults!!.first().name)
            assertEquals(bulbasaurDetails.name, result.value.detailedResults!![1].name)
        }

    @Test
    fun `fetchPokemonHome returns cache when network fails after successful load`() =
        runTest {
            val limit = 10
            val offset = 0
            val pikachuDetails =
                mockk<PokemonListDetailedItemResponse> {
                    every { name } returns "Pikachu"
                }
            val pokemonResults =
                listOf(
                    PokemonListItemResponse(name = "Pikachu", url = "https://pokeapi.co/api/v2/pokemon/1/"),
                )
            val pokemonListResponse =
                PokemonListResponse(
                    results = pokemonResults,
                    count = 1,
                    next = null,
                    previous = null,
                )

            coEvery { pokeHomeService.fetchHomePokemonDetail(any()) } returns pikachuDetails

            var listFetchCount = 0
            coEvery { pokeHomeService.fetchPokemonList(limit, offset) } answers {
                when (listFetchCount++) {
                    0 -> pokemonListResponse
                    else -> throw IOException("offline")
                }
            }

            val first = pokeHomeRepository.fetchPokemonHome(limit, offset)
            assertEquals(RemoteDataSource.NETWORK, first.source)
            val second = pokeHomeRepository.fetchPokemonHome(limit, offset)
            assertEquals(RemoteDataSource.CACHE, second.source)
            assertEquals("Pikachu", second.value.detailedResults!!.first().name)
        }

    @Test
    fun `Given a valid pokemon name when searching then return the corresponding pokemon details`() =
        runTest {
            // Arrange
            val pokemonName = "Pikachu"
            val expectedPokemonDetails =
                mockk<PokemonListDetailedItemResponse>(
                    relaxed = true,
                ) {
                    every { name } returns "Pikachu"
                    every { height } returns 4
                    every { weight } returns 60
                }

            coEvery { pokeHomeService.searchPokemon(pokemonName) } returns expectedPokemonDetails

            // Act
            val result = pokeHomeRepository.searchPokemon(pokemonName)

            // Assert
            assertEquals(RemoteDataSource.NETWORK, result.source)
            assertEquals(expectedPokemonDetails, result.value)
            assertEquals("Pikachu", result.value.name)
            assertEquals(4, result.value.height)
            assertEquals(60, result.value.weight)
        }

    @Test
    fun `Given a non-existent pokemon name when searching then throw an exception`() =
        runTest {
            // Arrange
            val pokemonName = "MissingNo"

            coEvery { pokeHomeService.searchPokemon(pokemonName) } throws NoSuchElementException("Pokemon not found")

            // Act & Assert
            try {
                pokeHomeRepository.searchPokemon(pokemonName)
            } catch (e: NoSuchElementException) {
                assertEquals("Pokemon not found", e.message)
            }
        }

    @Test
    fun `searchPokemon returns cache after IOException following successful search`() =
        runTest {
            val pokemonName = "Pikachu"
            val cachedDetails =
                mockk<PokemonListDetailedItemResponse>(relaxed = true) {
                    every { name } returns "Pikachu"
                }

            var searchCount = 0
            coEvery { pokeHomeService.searchPokemon(pokemonName) } answers {
                when (searchCount++) {
                    0 -> cachedDetails
                    else -> throw IOException("offline")
                }
            }

            val first = pokeHomeRepository.searchPokemon(pokemonName)
            assertEquals(RemoteDataSource.NETWORK, first.source)

            val second = pokeHomeRepository.searchPokemon(pokemonName)
            assertEquals(RemoteDataSource.CACHE, second.source)
            assertEquals("Pikachu", second.value.name)
        }

    @Test
    fun `searchPokemon returns cache after HttpException following successful search`() =
        runTest {
            val pokemonName = "Pikachu"
            val cachedDetails =
                mockk<PokemonListDetailedItemResponse>(relaxed = true) {
                    every { name } returns "Pikachu"
                }
            val httpException = mockk<HttpException>()

            var searchCount = 0
            coEvery { pokeHomeService.searchPokemon(pokemonName) } answers {
                when (searchCount++) {
                    0 -> cachedDetails
                    else -> throw httpException
                }
            }

            pokeHomeRepository.searchPokemon(pokemonName)
            val second = pokeHomeRepository.searchPokemon(pokemonName)

            assertEquals(RemoteDataSource.CACHE, second.source)
        }

    @Test
    fun `fetchPokemonHome throws when cache miss on failure`() =
        runTest {
            coEvery { pokeHomeService.fetchPokemonList(10, 99) } throws IOException("offline")

            try {
                pokeHomeRepository.fetchPokemonHome(10, 99)
                fail("Expected IOException")
            } catch (e: IOException) {
                assertEquals("offline", e.message)
            }
        }

    @Test
    fun `fetchPokemonHome returns cache when HttpException after successful load`() =
        runTest {
            val limit = 10
            val offset = 0
            val pikachuDetails =
                mockk<PokemonListDetailedItemResponse> {
                    every { name } returns "Pikachu"
                }
            val pokemonListResponse =
                PokemonListResponse(
                    results = listOf(PokemonListItemResponse("Pikachu", "https://pokeapi.co/api/v2/pokemon/1/")),
                    count = 1,
                    next = null,
                    previous = null,
                )
            val httpException = mockk<HttpException>()

            coEvery { pokeHomeService.fetchHomePokemonDetail(any()) } returns pikachuDetails

            var listFetchCount = 0
            coEvery { pokeHomeService.fetchPokemonList(limit, offset) } answers {
                when (listFetchCount++) {
                    0 -> pokemonListResponse
                    else -> throw httpException
                }
            }

            pokeHomeRepository.fetchPokemonHome(limit, offset)
            val second = pokeHomeRepository.fetchPokemonHome(limit, offset)

            assertEquals(RemoteDataSource.CACHE, second.source)
        }

    @Test
    fun `searchPokemon normalizes cache key with trim and lowercase`() =
        runTest {
            val cachedDetails =
                mockk<PokemonListDetailedItemResponse>(relaxed = true) {
                    every { name } returns "Pikachu"
                }

            coEvery { pokeHomeService.searchPokemon("  PiKaChu  ") } returns cachedDetails
            coEvery { pokeHomeService.searchPokemon("pikachu") } throws IOException("offline")

            pokeHomeRepository.searchPokemon("  PiKaChu  ")
            val cached = pokeHomeRepository.searchPokemon("pikachu")

            assertEquals(RemoteDataSource.CACHE, cached.source)
        }

    @Test
    fun `fetchPokemonHome rethrows CancellationException`() =
        runTest {
            coEvery { pokeHomeService.fetchPokemonList(10, 0) } throws CancellationException()

            try {
                pokeHomeRepository.fetchPokemonHome(10, 0)
                fail("Expected CancellationException")
            } catch (e: CancellationException) {
                assertEquals(CancellationException::class.java, e.javaClass)
            }
        }

    @Test
    fun `searchPokemon rethrows CancellationException`() =
        runTest {
            coEvery { pokeHomeService.searchPokemon("Pikachu") } throws CancellationException()

            try {
                pokeHomeRepository.searchPokemon("Pikachu")
                fail("Expected CancellationException")
            } catch (e: CancellationException) {
                assertEquals(CancellationException::class.java, e.javaClass)
            }
        }

    @Test
    fun `fetchPokemonHome uses separate cache keys per pagination offset`() =
        runTest {
            val pageZeroDetails =
                mockk<PokemonListDetailedItemResponse> {
                    every { name } returns "Pikachu"
                }
            val pageOneDetails =
                mockk<PokemonListDetailedItemResponse> {
                    every { name } returns "Bulbasaur"
                }

            coEvery { pokeHomeService.fetchPokemonList(10, 0) } returns
                PokemonListResponse(
                    results = listOf(PokemonListItemResponse("Pikachu", "https://pokeapi.co/api/v2/pokemon/1/")),
                    count = 2,
                    next = null,
                    previous = null,
                )
            coEvery { pokeHomeService.fetchPokemonList(10, 10) } returns
                PokemonListResponse(
                    results = listOf(PokemonListItemResponse("Bulbasaur", "https://pokeapi.co/api/v2/pokemon/2/")),
                    count = 2,
                    next = null,
                    previous = null,
                )
            coEvery { pokeHomeService.fetchHomePokemonDetail("https://pokeapi.co/api/v2/pokemon/1/") } returns
                pageZeroDetails
            coEvery { pokeHomeService.fetchHomePokemonDetail("https://pokeapi.co/api/v2/pokemon/2/") } returns
                pageOneDetails

            val firstPage = pokeHomeRepository.fetchPokemonHome(10, 0)
            val secondPage = pokeHomeRepository.fetchPokemonHome(10, 10)

            assertEquals("Pikachu", firstPage.value.detailedResults!!.first().name)
            assertEquals("Bulbasaur", secondPage.value.detailedResults!!.first().name)
        }
}
