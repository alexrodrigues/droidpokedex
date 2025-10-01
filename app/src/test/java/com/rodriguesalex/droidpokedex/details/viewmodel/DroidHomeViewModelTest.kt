package com.rodriguesalex.droidpokedex.details.viewmodel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.rodriguesalex.details.domain.usecase.GetPokeDetailsUseCase
import com.rodriguesalex.details.domain.model.PokemonDetails
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DroidHomeViewModelTest {
    @MockK lateinit var getPokeDetailsUseCase: GetPokeDetailsUseCase

    private lateinit var viewModel: DroidDetailsViewModel
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(dispatcher)
        viewModel = DroidDetailsViewModel(getPokeDetailsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() =
        runTest {
            val state = viewModel.detailsStateFlow.value
            assertTrue(state is DroidDetailsUiState.Loading)
        }

    @Test
    fun `loadPokemonDetails with invalid id sets Error and does not call use case`() =
        runTest {
            // Act
            viewModel.loadPokemonDetails("abc") // non-numeric
            // Assert
            val state = viewModel.detailsStateFlow.value
            assertTrue(state is DroidDetailsUiState.Error)
            coVerify(exactly = 0) { getPokeDetailsUseCase.invoke(any()) }
        }

    @Test
    fun `loadPokemonDetails emits Loading then Success on happy path`() =
        runTest {
            val id = 25
            val expected = mockk<PokemonDetails>(relaxed = true)

            coEvery {
                getPokeDetailsUseCase.invoke(GetPokeDetailsUseCase.Params(id))
            } returns expected

            // Act — call and check immediate Loading
            viewModel.loadPokemonDetails(id.toString())
            // Immediately after calling, state should be Loading
            assertTrue(viewModel.detailsStateFlow.value is DroidDetailsUiState.Loading)

            // Let the coroutine finish
            advanceUntilIdle()

            // Assert final state
            val state = viewModel.detailsStateFlow.value
            assertTrue(state is DroidDetailsUiState.Success)

            coVerify(exactly = 1) {
                getPokeDetailsUseCase.invoke(GetPokeDetailsUseCase.Params(id))
            }
        }

    @Test
    fun `loadPokemonDetails emits Loading then Error on failure`() =
        runTest {
            val id = 7

            coEvery {
                getPokeDetailsUseCase.invoke(GetPokeDetailsUseCase.Params(id))
            } throws RuntimeException("boom")

            // Act
            viewModel.loadPokemonDetails(id.toString())
            // Immediately Loading
            assertTrue(viewModel.detailsStateFlow.value is DroidDetailsUiState.Loading)

            advanceUntilIdle()

            // Assert Error
            val state = viewModel.detailsStateFlow.value
            assertTrue(state is DroidDetailsUiState.Error)

            coVerify(exactly = 1) {
                getPokeDetailsUseCase.invoke(GetPokeDetailsUseCase.Params(id))
            }
        }

    @Test
    fun `onRetry re-runs the load flow`() =
        runTest {
            val id = 10
            val expected = mockk<PokemonDetails>(relaxed = true)

            // First attempt fails
            coEvery {
                getPokeDetailsUseCase.invoke(GetPokeDetailsUseCase.Params(id))
            } throws RuntimeException("first failure")

            viewModel.loadPokemonDetails(id.toString())
            advanceUntilIdle()
            assertTrue(viewModel.detailsStateFlow.value is DroidDetailsUiState.Error)

            // Second attempt succeeds
            coEvery {
                getPokeDetailsUseCase.invoke(GetPokeDetailsUseCase.Params(id))
            } returns expected

            viewModel.onRetry(id.toString())
            // Immediately Loading again
            assertTrue(viewModel.detailsStateFlow.value is DroidDetailsUiState.Loading)

            advanceUntilIdle()

            val state = viewModel.detailsStateFlow.value
            assertTrue(state is DroidDetailsUiState.Success)

            coVerify(exactly = 2) {
                getPokeDetailsUseCase.invoke(GetPokeDetailsUseCase.Params(id))
            }
        }
}
