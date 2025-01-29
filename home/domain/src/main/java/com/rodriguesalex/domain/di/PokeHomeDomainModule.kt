package com.rodriguesalex.domain.di

import com.rodriguesalex.domain.repository.PokeHomeRepository
import com.rodriguesalex.domain.usecase.GetPokeHomeUseCase
import com.rodriguesalex.domain.usecase.SearchPokemonUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PokeHomeDomainModule {
    @Provides
    @Singleton
    fun providesGetPokeHomeUseCase(
        repository: PokeHomeRepository
    ): GetPokeHomeUseCase = GetPokeHomeUseCase(repository = repository)

    @Provides
    @Singleton
    fun providesSearchPokemonUseCase(
        repository: PokeHomeRepository
    ): SearchPokemonUseCase = SearchPokemonUseCase(repository = repository)
}
