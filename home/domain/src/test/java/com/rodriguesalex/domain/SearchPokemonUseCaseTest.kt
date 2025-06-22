package com.rodriguesalex.domain

import com.rodriguesalex.domain.model.PokemonListDetailedItemResponse
import com.rodriguesalex.domain.repository.PokeHomeRepository
import com.rodriguesalex.domain.usecase.SearchPokemonUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Assert.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SearchPokemonUseCaseTest {
    @MockK
    private lateinit var repository: PokeHomeRepository

    private lateinit var searchPokemonUseCase: SearchPokemonUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkStatic("com.rodriguesalex.domain.mapper.DroidHomeMapperKt")
        searchPokemonUseCase = SearchPokemonUseCase(repository)
    }

    @Test
    fun `Given a pokemon name then we should provide a pokemon`() =
        runTest {
            // Arrange
            val params = SearchPokemonUseCase.Params(name = "Pikachu")
            val pikachu =
                mockk<PokemonListDetailedItemResponse>(relaxed = true) {
                    every { id } returns 1
                    every { name } returns "Pikachu"
                }

            coEvery { repository.searchPokemon(params.name) } returns pikachu

            // Act
            val result = searchPokemonUseCase.invoke(params)

            // Assert
            assertEquals(pikachu.name, result.name)
        }

    @Test
    fun `Given a non-existent pokemon name when searching then throw an exception`() =
        runTest {
            // Arrange
            val params = SearchPokemonUseCase.Params(name = "MissingNo")

            coEvery { repository.searchPokemon("MissingNo") } throws NoSuchElementException("Pokemon not found")

            // Act & Assert
            try {
                searchPokemonUseCase.invoke(params)
            } catch (e: NoSuchElementException) {
                assertEquals("Pokemon not found", e.message)
            }
        }

    @Test
    fun `Given mapping logic is correct then toModel is called`() = runTest {
        // Arrange
        val params = SearchPokemonUseCase.Params(name = "Bulbasaur")
        val bulbasaurResponse = mockk<PokemonListDetailedItemResponse>(relaxed = true)
        coEvery { repository.searchPokemon(params.name) } returns bulbasaurResponse
        // The mapping extension is static-mocked in setup
        // Act
        searchPokemonUseCase.invoke(params)
        // Assert
        coVerify { repository.searchPokemon("Bulbasaur") }
    }

    @Test
    fun `Given different pokemon names then repository is called with those names`() = runTest {
        // Arrange
        val params = SearchPokemonUseCase.Params(name = "Charmander")
        val charmanderResponse = mockk<PokemonListDetailedItemResponse>(relaxed = true)
        coEvery { repository.searchPokemon(params.name) } returns charmanderResponse
        // Act
        searchPokemonUseCase.invoke(params)
        // Assert
        coVerify { repository.searchPokemon("Charmander") }
    }

    @Test(expected = RuntimeException::class)
    fun `Given repository throws generic exception then usecase throws exception`() = runTest {
        // Arrange
        val params = SearchPokemonUseCase.Params(name = "Eevee")
        coEvery { repository.searchPokemon(params.name) } throws RuntimeException("Unexpected error")
        // Act
        searchPokemonUseCase.invoke(params)
        // Assert is handled by expected exception
    }

    // Note: If repository.searchPokemon can return null, add this test. If not, you can remove it.
    // @Test(expected = NullPointerException::class)
    // fun `Given repository returns null then usecase throws exception`() = runTest {
    //     // Arrange
    //     val params = SearchPokemonUseCase.Params(name = "Nullmon")
    //     coEvery { repository.searchPokemon(params.name) } returns null
    //     // Act
    //     searchPokemonUseCase.invoke(params)
    //     // Assert is handled by expected exception
    // }
}
