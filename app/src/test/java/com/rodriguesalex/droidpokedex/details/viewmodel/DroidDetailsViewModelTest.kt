package com.rodriguesalex.droidpokedex.details.viewmodel

import com.rodriguesalex.details.domain.model.DetailsFetchOutcome
import com.rodriguesalex.details.domain.model.DetailsRemoteSource
import com.rodriguesalex.details.domain.model.PokemonDetails
import com.rodriguesalex.details.domain.usecase.GetPokeDetailsUseCase
import com.rodriguesalex.droidpokedex.R
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DroidDetailsViewModelTest {
    @MockK
    private lateinit var getPokeDetailsUseCase: GetPokeDetailsUseCase

    private lateinit var viewModel: DroidDetailsViewModel

    private val testDispatcher = StandardTestDispatcher()

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
    fun `loadPokemonDetails with valid id emits Success`() =
        runTest {
            val details = minimalPokemonDetails(name = "bulbasaur")
            coEvery { getPokeDetailsUseCase.invoke(GetPokeDetailsUseCase.Params(id = 1)) } returns
                DetailsFetchOutcome(details, DetailsRemoteSource.NETWORK)

            viewModel = DroidDetailsViewModel(getPokeDetailsUseCase)
            viewModel.loadPokemonDetails("1")
            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.detailsStateFlow.value
            assertTrue(state is DroidDetailsUiState.Success)
            assertEquals("bulbasaur", (state as DroidDetailsUiState.Success).pokemonDetails.name)
            assertFalse(state.isOfflineData)
        }

    @Test
    fun `loadPokemonDetails with invalid id emits Error`() =
        runTest {
            viewModel = DroidDetailsViewModel(getPokeDetailsUseCase)
            viewModel.loadPokemonDetails("not-a-number")
            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.detailsStateFlow.value
            assertTrue(state is DroidDetailsUiState.Error)
            assertEquals(
                R.string.error_invalid_id_title,
                (state as DroidDetailsUiState.Error).info.titleRes,
            )
            coVerify(exactly = 0) { getPokeDetailsUseCase.invoke(any()) }
        }

    @Test
    fun `loadPokemonDetails sets isOfflineData when source is CACHE`() =
        runTest {
            val details = minimalPokemonDetails()
            coEvery { getPokeDetailsUseCase.invoke(GetPokeDetailsUseCase.Params(id = 1)) } returns
                DetailsFetchOutcome(details, DetailsRemoteSource.CACHE)

            viewModel = DroidDetailsViewModel(getPokeDetailsUseCase)
            viewModel.loadPokemonDetails("1")
            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.detailsStateFlow.value as DroidDetailsUiState.Success
            assertTrue(state.isOfflineData)
        }

    @Test
    fun `loadPokemonDetails emits Error on use case failure`() =
        runTest {
            coEvery { getPokeDetailsUseCase.invoke(GetPokeDetailsUseCase.Params(id = 1)) } throws
                java.io.IOException("offline")

            viewModel = DroidDetailsViewModel(getPokeDetailsUseCase)
            viewModel.loadPokemonDetails("1")
            testDispatcher.scheduler.advanceUntilIdle()

            assertTrue(viewModel.detailsStateFlow.value is DroidDetailsUiState.Error)
        }

    @Test
    fun `onRetry reloads after failure`() =
        runTest {
            val details = minimalPokemonDetails()
            var callCount = 0
            coEvery { getPokeDetailsUseCase.invoke(GetPokeDetailsUseCase.Params(id = 1)) } answers {
                if (callCount++ == 0) error("fail") else DetailsFetchOutcome(details, DetailsRemoteSource.NETWORK)
            }

            viewModel = DroidDetailsViewModel(getPokeDetailsUseCase)
            viewModel.loadPokemonDetails("1")
            testDispatcher.scheduler.advanceUntilIdle()
            assertTrue(viewModel.detailsStateFlow.value is DroidDetailsUiState.Error)

            viewModel.onRetry("1")
            testDispatcher.scheduler.advanceUntilIdle()
            assertTrue(viewModel.detailsStateFlow.value is DroidDetailsUiState.Success)
        }

    @Test
    fun `loadPokemonDetails ignores concurrent load while loading`() =
        runTest {
            val details = minimalPokemonDetails()
            coEvery { getPokeDetailsUseCase.invoke(GetPokeDetailsUseCase.Params(id = 1)) } coAnswers {
                kotlinx.coroutines.delay(1_000)
                DetailsFetchOutcome(details, DetailsRemoteSource.NETWORK)
            }

            viewModel = DroidDetailsViewModel(getPokeDetailsUseCase)
            viewModel.loadPokemonDetails("1")
            viewModel.loadPokemonDetails("1")

            testDispatcher.scheduler.advanceUntilIdle()

            coVerify(exactly = 1) { getPokeDetailsUseCase.invoke(GetPokeDetailsUseCase.Params(id = 1)) }
        }
}

private fun minimalPokemonDetails(
    id: Int = 1,
    name: String = "bulbasaur",
): PokemonDetails =
    PokemonDetails(
        id = id,
        name = name,
        baseExperience = 64,
        heightMeters = 0.7,
        weightKg = 6.9,
        types = listOf("grass"),
        abilities = emptyList(),
        stats = mapOf("hp" to 45),
        sprites =
            PokemonDetails.Sprites(
                frontDefault = null,
                backDefault = null,
                frontShiny = null,
                backShiny = null,
            ),
        officialArtworkUrl = "https://example.com/1.png",
        genus = null,
        flavorText = null,
        evolutionChain = emptyList(),
    )
