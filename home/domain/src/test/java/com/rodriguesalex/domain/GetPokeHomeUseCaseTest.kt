package com.rodriguesalex.domain

import com.rodriguesalex.domain.mapper.toModel
import com.rodriguesalex.domain.model.PokemonList
import com.rodriguesalex.domain.model.PokemonListItem
import com.rodriguesalex.domain.model.PokemonListResponse
import com.rodriguesalex.domain.repository.PokeHomeRepository
import com.rodriguesalex.domain.usecase.GetPokeHomeUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkStatic
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetPokeHomeUseCaseTest {
    @MockK
    private lateinit var repository: PokeHomeRepository

    private lateinit var getPokeHomeUseCase: GetPokeHomeUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkStatic("com.rodriguesalex.domain.mapper.DroidHomeMapperKt")
        getPokeHomeUseCase = GetPokeHomeUseCase(repository)
    }

    @Suppress("LongMethod")
    @Test
    fun `Given our pokedex its open then we should provide pokemons`() =
        runTest {
            // Arrange
            val params = GetPokeHomeUseCase.Params(limit = 10, offset = 0)
            val pikachu =
                mockk<PokemonListItem> {
                    every { name } returns "Pikachu"
                }

            val bulbasaur =
                mockk<PokemonListItem> {
                    every { name } returns "Bulbasaur"
                }

            val mockedResponse =
                mockk<PokemonListResponse> {
                    coEvery { toModel() } returns
                        PokemonList(
                            results = listOf(pikachu, bulbasaur),
                            count = 10,
                            next = "https://pokeapi.co/api/v2/pokemon?offset=10&limit=10",
                            previous = null,
                        )
                }

            coEvery { repository.fetchPokemonHome(params.limit, params.offset) } returns mockedResponse

            // Act
            val result = getPokeHomeUseCase.invoke(params)

            // Assert
            assertEquals(2, result.results.size)
            assertEquals("Pikachu", result.results.first().name)
            assertEquals("Bulbasaur", result.results[1].name)
        }
}
