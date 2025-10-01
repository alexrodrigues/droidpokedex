package com.rodriguesalex.details.data.repository

import com.rodriguesalex.details.data.service.PokeDetailsService
import com.rodriguesalex.details.domain.model.PokemonDetailsResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PokeDetailsRepositoryImplTest {
    @MockK lateinit var service: PokeDetailsService

    private lateinit var repository: PokeDetailsRepositoryImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        repository = PokeDetailsRepositoryImpl(service)
    }

    @Test
    fun `fetchPokemonDetails calls service with correct id`() =
        runTest {
            // Arrange
            val id = 25
            val expected: PokemonDetailsResponse = mockk(relaxed = true)
            coEvery { service.fetchPokemonDetails(id) } returns expected

            // Act
            repository.fetchPokemonDetails(id)

            // Assert
            coVerify(exactly = 1) { service.fetchPokemonDetails(id) }
        }

    @Test
    fun `fetchPokemonDetails returns the same result from service`() =
        runTest {
            // Arrange
            val id = 7
            val expected: PokemonDetailsResponse = mockk(relaxed = true)
            coEvery { service.fetchPokemonDetails(id) } returns expected

            // Act
            val result = repository.fetchPokemonDetails(id)

            // Assert
            assertSame("Repository should return the exact instance from the service", expected, result)
            coVerify(exactly = 1) { service.fetchPokemonDetails(id) }
        }

    @Test
    fun `fetchPokemonDetails propagates exceptions from service`() =
        runTest {
            // Arrange
            val id = 99
            val boom = RuntimeException("boom")
            coEvery { service.fetchPokemonDetails(id) } throws boom

            // Act + Assert
            try {
                repository.fetchPokemonDetails(id)
                throw AssertionError("Expected exception to be thrown")
            } catch (e: RuntimeException) {
                assertEquals("boom", e.message)
            }

            coVerify(exactly = 1) { service.fetchPokemonDetails(id) }
        }
}
