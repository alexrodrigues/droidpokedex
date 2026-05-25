package com.rodriguesalex.details.domain.usecase

import com.rodriguesalex.details.domain.bulbasaurAggregate
import com.rodriguesalex.details.domain.mapper.toDomain
import com.rodriguesalex.details.domain.model.DetailsFetchOutcome
import com.rodriguesalex.details.domain.model.DetailsRemoteSource
import com.rodriguesalex.details.domain.repository.PokeDetailsRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetPokeDetailsUseCaseTest {
    @MockK
    private lateinit var repository: PokeDetailsRepository

    private lateinit var useCase: GetPokeDetailsUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        useCase = GetPokeDetailsUseCase(repository)
    }

    @Test
    fun `invoke maps aggregate to PokemonDetails`() =
        runTest {
            val aggregate = bulbasaurAggregate()
            val expected = aggregate.toDomain()

            coEvery { repository.fetchPokemonDetails(1) } returns
                DetailsFetchOutcome(aggregate, DetailsRemoteSource.NETWORK)

            val result = useCase.invoke(GetPokeDetailsUseCase.Params(id = 1))

            assertEquals(expected.name, result.value.name)
            assertEquals(expected.genus, result.value.genus)
            assertEquals(expected.evolutionChain.size, result.value.evolutionChain.size)
        }

    @Test
    fun `invoke preserves NETWORK source`() =
        runTest {
            coEvery { repository.fetchPokemonDetails(1) } returns
                DetailsFetchOutcome(bulbasaurAggregate(), DetailsRemoteSource.NETWORK)

            val result = useCase.invoke(GetPokeDetailsUseCase.Params(id = 1))

            assertEquals(DetailsRemoteSource.NETWORK, result.source)
        }

    @Test
    fun `invoke preserves CACHE source`() =
        runTest {
            coEvery { repository.fetchPokemonDetails(1) } returns
                DetailsFetchOutcome(bulbasaurAggregate(), DetailsRemoteSource.CACHE)

            val result = useCase.invoke(GetPokeDetailsUseCase.Params(id = 1))

            assertEquals(DetailsRemoteSource.CACHE, result.source)
        }

    @Test
    fun `invoke calls repository with param id`() =
        runTest {
            coEvery { repository.fetchPokemonDetails(1) } returns
                DetailsFetchOutcome(bulbasaurAggregate(), DetailsRemoteSource.NETWORK)

            useCase.invoke(GetPokeDetailsUseCase.Params(id = 1))

            coVerify { repository.fetchPokemonDetails(1) }
        }

    @Test(expected = RuntimeException::class)
    fun `invoke propagates repository exception`() =
        runTest {
            coEvery { repository.fetchPokemonDetails(1) } throws RuntimeException("Network error")

            useCase.invoke(GetPokeDetailsUseCase.Params(id = 1))
        }
}
