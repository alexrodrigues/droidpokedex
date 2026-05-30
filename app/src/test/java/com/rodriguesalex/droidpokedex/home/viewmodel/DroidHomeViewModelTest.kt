package com.rodriguesalex.droidpokedex.home.viewmodel

import androidx.compose.ui.graphics.Color
import com.rodriguesalex.droidpokedex.R
import com.rodriguesalex.domain.model.FetchOutcome
import com.rodriguesalex.domain.model.PokemonList
import com.rodriguesalex.domain.model.PokemonListItem
import com.rodriguesalex.domain.model.NamedAPIResource
import com.rodriguesalex.domain.model.RemoteDataSource
import com.rodriguesalex.domain.model.Sprites
import com.rodriguesalex.domain.model.TypeSlot
import com.rodriguesalex.domain.usecase.GetPokeHomeUseCase
import com.rodriguesalex.domain.usecase.SearchPokemonUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException

@OptIn(ExperimentalCoroutinesApi::class)
class DroidHomeViewModelTest {
    @MockK
    private lateinit var getHomeUseCase: GetPokeHomeUseCase

    @MockK
    private lateinit var searchPokemonUseCase: SearchPokemonUseCase

    private lateinit var viewModel: DroidHomeViewModel

    private val testDispatcher = StandardTestDispatcher()

    private val mockPokemonList =
        listOf(
            createMockPokemon(1, "Pikachu"),
            createMockPokemon(2, "Bulbasaur"),
            createMockPokemon(3, "Charmander"),
        )

    private val mockPokemonList2 =
        listOf(
            createMockPokemon(4, "Squirtle"),
            createMockPokemon(5, "Caterpie"),
        )

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should load initial pokemon list`() =
        runTest {
            // Arrange
            val expectedPokemonList =
                PokemonList(
                    count = 3,
                    next = "https://pokeapi.co/api/v2/pokemon?offset=20&limit=20",
                    previous = null,
                    results = mockPokemonList,
                )

            coEvery {
                getHomeUseCase.invoke(GetPokeHomeUseCase.Params(limit = 20, offset = 0))
            } returns FetchOutcome(expectedPokemonList, RemoteDataSource.NETWORK)

            // Act
            viewModel = DroidHomeViewModel(getHomeUseCase, searchPokemonUseCase)
            testDispatcher.scheduler.advanceUntilIdle()

            // Assert
            val currentState = viewModel.homeStateFlow.value
            assertTrue(currentState is DroidHomeUiState.Success)
            val successState = currentState as DroidHomeUiState.Success
            assertEquals(3, successState.pokemons.size)
            assertEquals("Pikachu", successState.pokemons[0].name)
            assertEquals("Bulbasaur", successState.pokemons[1].name)
            assertEquals("Charmander", successState.pokemons[2].name)
            assertFalse(successState.isSearching)
        }

    @Test
    fun `loadMorePokemons should append new pokemons to existing list`() =
        runTest {
            // Arrange - Initial load
            val initialPokemonList =
                PokemonList(
                    count = 3,
                    next = "https://pokeapi.co/api/v2/pokemon?offset=20&limit=20",
                    previous = null,
                    results = mockPokemonList,
                )

            coEvery {
                getHomeUseCase.invoke(GetPokeHomeUseCase.Params(limit = 20, offset = 0))
            } returns FetchOutcome(initialPokemonList, RemoteDataSource.NETWORK)

            // Act - Initial load
            viewModel = DroidHomeViewModel(getHomeUseCase, searchPokemonUseCase)
            testDispatcher.scheduler.advanceUntilIdle()

            // Arrange - Second page
            val secondPagePokemonList =
                PokemonList(
                    count = 5,
                    next = "https://pokeapi.co/api/v2/pokemon?offset=40&limit=20",
                    previous = "https://pokeapi.co/api/v2/pokemon?offset=0&limit=20",
                    results = mockPokemonList2,
                )

            coEvery {
                getHomeUseCase.invoke(GetPokeHomeUseCase.Params(limit = 20, offset = 20))
            } returns FetchOutcome(secondPagePokemonList, RemoteDataSource.NETWORK)

            // Act - Load more
            viewModel.loadMorePokemons()
            testDispatcher.scheduler.advanceUntilIdle()

            // Assert
            val currentState = viewModel.homeStateFlow.value
            assertTrue(currentState is DroidHomeUiState.Success)
            val successState = currentState as DroidHomeUiState.Success
            assertEquals(5, successState.pokemons.size)
            assertEquals("Pikachu", successState.pokemons[0].name)
            assertEquals("Squirtle", successState.pokemons[3].name)
            assertEquals("Caterpie", successState.pokemons[4].name)
        }

    @Test
    fun `loadMorePokemons should handle error state`() =
        runTest {
            // Arrange
            coEvery {
                getHomeUseCase.invoke(GetPokeHomeUseCase.Params(limit = 20, offset = 0))
            } throws RuntimeException("Network error")

            // Act
            viewModel = DroidHomeViewModel(getHomeUseCase, searchPokemonUseCase)
            testDispatcher.scheduler.advanceUntilIdle()

            // Assert
            val currentState = viewModel.homeStateFlow.value
            assertTrue(currentState is DroidHomeUiState.Error)
        }

    @Test
    fun `onSearchQueryChanged with empty query should reset to initial state and load pokemons`() =
        runTest {
            // Arrange - Initial load
            val initialPokemonList =
                PokemonList(
                    count = 3,
                    next = "https://pokeapi.co/api/v2/pokemon?offset=20&limit=20",
                    previous = null,
                    results = mockPokemonList,
                )

            coEvery {
                getHomeUseCase.invoke(GetPokeHomeUseCase.Params(limit = 20, offset = 0))
            } returns FetchOutcome(initialPokemonList, RemoteDataSource.NETWORK)

            viewModel = DroidHomeViewModel(getHomeUseCase, searchPokemonUseCase)
            testDispatcher.scheduler.advanceUntilIdle()

            // Arrange - Reset load
            coEvery {
                getHomeUseCase.invoke(GetPokeHomeUseCase.Params(limit = 20, offset = 0))
            } returns FetchOutcome(initialPokemonList, RemoteDataSource.NETWORK)

            // Act
            viewModel.onSearchQueryChanged("")
            testDispatcher.scheduler.advanceUntilIdle()

            // Assert
            assertEquals("", viewModel.searchQuery.value)
            val currentState = viewModel.homeStateFlow.value
            assertTrue(currentState is DroidHomeUiState.Success)
            val successState = currentState as DroidHomeUiState.Success
            assertEquals(3, successState.pokemons.size)
            assertFalse(successState.isSearching)
        }

    @Test
    fun `onSearchQueryChanged with valid query should search for pokemon`() =
        runTest {
            // Arrange - Initial load
            val initialPokemonList =
                PokemonList(
                    count = 3,
                    next = "https://pokeapi.co/api/v2/pokemon?offset=20&limit=20",
                    previous = null,
                    results = mockPokemonList,
                )

            coEvery {
                getHomeUseCase.invoke(GetPokeHomeUseCase.Params(limit = 20, offset = 0))
            } returns FetchOutcome(initialPokemonList, RemoteDataSource.NETWORK)

            viewModel = DroidHomeViewModel(getHomeUseCase, searchPokemonUseCase)
            testDispatcher.scheduler.advanceUntilIdle()

            // Arrange - Search
            val searchQuery = "Pikachu"
            val searchedPokemon = createMockPokemon(1, "Pikachu")

            coEvery {
                searchPokemonUseCase.invoke(SearchPokemonUseCase.Params(searchQuery))
            } returns FetchOutcome(searchedPokemon, RemoteDataSource.NETWORK)

            // Act
            viewModel.onSearchQueryChanged(searchQuery)
            testDispatcher.scheduler.advanceUntilIdle()

            // Assert
            assertEquals(searchQuery, viewModel.searchQuery.value)
            val currentState = viewModel.homeStateFlow.value
            assertTrue(currentState is DroidHomeUiState.Success)
            val successState = currentState as DroidHomeUiState.Success
            assertEquals(1, successState.pokemons.size)
            assertEquals("Pikachu", successState.pokemons[0].name)
            assertTrue(successState.isSearching)
        }

    @Test
    fun `onSearchQueryChanged with invalid query should show error state`() =
        runTest {
            // Arrange - Initial load
            val initialPokemonList =
                PokemonList(
                    count = 3,
                    next = "https://pokeapi.co/api/v2/pokemon?offset=20&limit=20",
                    previous = null,
                    results = mockPokemonList,
                )

            coEvery {
                getHomeUseCase.invoke(GetPokeHomeUseCase.Params(limit = 20, offset = 0))
            } returns FetchOutcome(initialPokemonList, RemoteDataSource.NETWORK)

            viewModel = DroidHomeViewModel(getHomeUseCase, searchPokemonUseCase)
            testDispatcher.scheduler.advanceUntilIdle()

            // Arrange - Search error
            val searchQuery = "InvalidPokemon"

            coEvery {
                searchPokemonUseCase.invoke(SearchPokemonUseCase.Params(searchQuery))
            } throws RuntimeException("Pokemon not found")

            // Act
            viewModel.onSearchQueryChanged(searchQuery)
            testDispatcher.scheduler.advanceUntilIdle()

            // Assert
            assertEquals(searchQuery, viewModel.searchQuery.value)
            val currentState = viewModel.homeStateFlow.value
            assertTrue(currentState is DroidHomeUiState.Error)
        }

    @Test
    fun `isLoading should be false after pokemon loading completes`() =
        runTest {
            // Arrange
            val initialPokemonList =
                PokemonList(
                    count = 3,
                    next = "https://pokeapi.co/api/v2/pokemon?offset=20&limit=20",
                    previous = null,
                    results = mockPokemonList,
                )

            coEvery {
                getHomeUseCase.invoke(GetPokeHomeUseCase.Params(limit = 20, offset = 0))
            } returns FetchOutcome(initialPokemonList, RemoteDataSource.NETWORK)

            // Act
            viewModel = DroidHomeViewModel(getHomeUseCase, searchPokemonUseCase)
            testDispatcher.scheduler.advanceUntilIdle()

            // Assert
            assertFalse(viewModel.isLoading.value)
        }

    @Test
    fun `loadMorePokemons should not load when already loading`() =
        runTest {
            // Arrange - Initial load
            val initialPokemonList =
                PokemonList(
                    count = 3,
                    next = "https://pokeapi.co/api/v2/pokemon?offset=20&limit=20",
                    previous = null,
                    results = mockPokemonList,
                )

            coEvery {
                getHomeUseCase.invoke(GetPokeHomeUseCase.Params(limit = 20, offset = 0))
            } returns FetchOutcome(initialPokemonList, RemoteDataSource.NETWORK)

            viewModel = DroidHomeViewModel(getHomeUseCase, searchPokemonUseCase)
            testDispatcher.scheduler.advanceUntilIdle()

            // Arrange - Second page (should not be called)
            coEvery {
                getHomeUseCase.invoke(GetPokeHomeUseCase.Params(limit = 20, offset = 20))
            } returns
                FetchOutcome(
                    PokemonList(
                        count = 5,
                        next = null,
                        previous = "https://pokeapi.co/api/v2/pokemon?offset=0&limit=20",
                        results = mockPokemonList2,
                    ),
                    RemoteDataSource.NETWORK,
                )

            // Act - Call loadMorePokemons multiple times rapidly
            viewModel.loadMorePokemons()
            viewModel.loadMorePokemons()
            viewModel.loadMorePokemons()
            testDispatcher.scheduler.advanceUntilIdle()

            // Assert - Should only load once
            val currentState = viewModel.homeStateFlow.value
            assertTrue(currentState is DroidHomeUiState.Success)
            val successState = currentState as DroidHomeUiState.Success
            assertEquals(5, successState.pokemons.size) // 3 initial + 2 from second page
        }

    @Test
    fun `loadMorePokemons sets isOfflineData when source is CACHE`() =
        runTest {
            val initialPokemonList =
                PokemonList(
                    count = 1,
                    next = null,
                    previous = null,
                    results = listOf(createMockPokemon(1, "Pikachu")),
                )

            coEvery {
                getHomeUseCase.invoke(GetPokeHomeUseCase.Params(limit = 20, offset = 0))
            } returns FetchOutcome(initialPokemonList, RemoteDataSource.CACHE)

            viewModel = DroidHomeViewModel(getHomeUseCase, searchPokemonUseCase)
            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.homeStateFlow.value as DroidHomeUiState.Success
            assertTrue(state.isOfflineData)
        }

    @Test
    fun `search with cache source sets isOfflineData`() =
        runTest {
            coEvery {
                getHomeUseCase.invoke(GetPokeHomeUseCase.Params(limit = 20, offset = 0))
            } returns
                FetchOutcome(
                    PokemonList(count = 0, next = null, previous = null, results = emptyList()),
                    RemoteDataSource.NETWORK,
                )

            viewModel = DroidHomeViewModel(getHomeUseCase, searchPokemonUseCase)
            testDispatcher.scheduler.advanceUntilIdle()

            val searchedPokemon = createMockPokemon(25, "Pikachu")
            coEvery {
                searchPokemonUseCase.invoke(SearchPokemonUseCase.Params("Pikachu"))
            } returns FetchOutcome(searchedPokemon, RemoteDataSource.CACHE)

            viewModel.onSearchQueryChanged("Pikachu")
            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.homeStateFlow.value as DroidHomeUiState.Success
            assertTrue(state.isSearching)
            assertTrue(state.isOfflineData)
        }

    @Test
    fun `onRetry after error reloads home list and clears search query`() =
        runTest {
            var callCount = 0
            coEvery {
                getHomeUseCase.invoke(GetPokeHomeUseCase.Params(limit = 20, offset = 0))
            } answers {
                if (callCount++ == 0) {
                    throw RuntimeException("fail")
                } else {
                    FetchOutcome(
                        PokemonList(
                            count = 1,
                            next = null,
                            previous = null,
                            results = listOf(createMockPokemon(1, "Pikachu")),
                        ),
                        RemoteDataSource.NETWORK,
                    )
                }
            }

            viewModel = DroidHomeViewModel(getHomeUseCase, searchPokemonUseCase)
            testDispatcher.scheduler.advanceUntilIdle()
            assertTrue(viewModel.homeStateFlow.value is DroidHomeUiState.Error)

            viewModel.onRetry()
            testDispatcher.scheduler.advanceUntilIdle()

            assertEquals("", viewModel.searchQuery.value)
            assertTrue(viewModel.homeStateFlow.value is DroidHomeUiState.Success)
        }

    @Test
    fun `loadMorePokemons maps HttpException to server error state`() =
        runTest {
            coEvery {
                getHomeUseCase.invoke(GetPokeHomeUseCase.Params(limit = 20, offset = 0))
            } throws
                mockk<HttpException> {
                    every { code() } returns 503
                }

            viewModel = DroidHomeViewModel(getHomeUseCase, searchPokemonUseCase)
            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.homeStateFlow.value as DroidHomeUiState.Error
            assertEquals(R.string.error_server_title, state.info.titleRes)
        }

    @Test
    fun `loadMorePokemons preserves offline flag when appending cached page`() =
        runTest {
            val firstPage =
                PokemonList(
                    count = 2,
                    next = "next",
                    previous = null,
                    results = mockPokemonList,
                )
            val secondPage =
                PokemonList(
                    count = 2,
                    next = null,
                    previous = "prev",
                    results = mockPokemonList2,
                )

            coEvery {
                getHomeUseCase.invoke(GetPokeHomeUseCase.Params(limit = 20, offset = 0))
            } returns FetchOutcome(firstPage, RemoteDataSource.CACHE)
            coEvery {
                getHomeUseCase.invoke(GetPokeHomeUseCase.Params(limit = 20, offset = 20))
            } returns FetchOutcome(secondPage, RemoteDataSource.NETWORK)

            viewModel = DroidHomeViewModel(getHomeUseCase, searchPokemonUseCase)
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.loadMorePokemons()
            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.homeStateFlow.value as DroidHomeUiState.Success
            assertEquals(5, state.pokemons.size)
            assertTrue(state.isOfflineData)
        }

    @Test
    fun `loadMorePokemons after initial success maps failure to error state`() =
        runTest {
            coEvery {
                getHomeUseCase.invoke(GetPokeHomeUseCase.Params(limit = 20, offset = 0))
            } returns
                FetchOutcome(
                    PokemonList(
                        count = 2,
                        next = "next",
                        previous = null,
                        results = mockPokemonList,
                    ),
                    RemoteDataSource.NETWORK,
                )
            coEvery {
                getHomeUseCase.invoke(GetPokeHomeUseCase.Params(limit = 20, offset = 20))
            } throws RuntimeException("page two failed")

            viewModel = DroidHomeViewModel(getHomeUseCase, searchPokemonUseCase)
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.loadMorePokemons()
            testDispatcher.scheduler.advanceUntilIdle()

            assertTrue(viewModel.homeStateFlow.value is DroidHomeUiState.Error)
        }

    @Test
    fun `search maps HttpException to server error state`() =
        runTest {
            coEvery {
                getHomeUseCase.invoke(GetPokeHomeUseCase.Params(limit = 20, offset = 0))
            } returns
                FetchOutcome(
                    PokemonList(count = 0, next = null, previous = null, results = emptyList()),
                    RemoteDataSource.NETWORK,
                )

            viewModel = DroidHomeViewModel(getHomeUseCase, searchPokemonUseCase)
            testDispatcher.scheduler.advanceUntilIdle()

            coEvery {
                searchPokemonUseCase.invoke(SearchPokemonUseCase.Params("Pikachu"))
            } throws
                mockk<HttpException> {
                    every { code() } returns 404
                }

            viewModel.onSearchQueryChanged("Pikachu")
            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.homeStateFlow.value as DroidHomeUiState.Error
            assertEquals(R.string.error_server_title, state.info.titleRes)
            assertEquals(404, state.info.detailArg)
        }

    private fun createMockPokemon(
        id: Int,
        name: String,
    ): PokemonListItem {
        return PokemonListItem(
            id = id,
            name = name,
            pokemonImageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png",
            backgroundColor = Color.Blue,
            baseExperience = 64,
            height = 7,
            isDefault = true,
            order = id,
            weight = 69,
            abilities = emptyList(),
            forms = emptyList(),
            gameIndices = emptyList(),
            heldItems = emptyList(),
            locationAreaEncounters = "",
            moves = emptyList(),
            species = NamedAPIResource(name = name.lowercase(), url = "https://pokeapi.co/api/v2/pokemon-species/$id/"),
            sprites =
                Sprites(
                    backDefault = null,
                    backFemale = null,
                    backShiny = null,
                    backShinyFemale = null,
                    frontDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png",
                    frontFemale = null,
                    frontShiny = null,
                    frontShinyFemale = null,
                ),
            stats = emptyList(),
            types =
                listOf(
                    TypeSlot(
                        slot = 1,
                        type = NamedAPIResource(name = "electric", url = "https://pokeapi.co/api/v2/type/13/"),
                    ),
                ),
        )
    }
}
