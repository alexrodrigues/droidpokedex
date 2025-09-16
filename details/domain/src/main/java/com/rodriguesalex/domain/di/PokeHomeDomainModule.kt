package com.rodriguesalex.domain.di

import com.rodriguesalex.domain.repository.PokeDetailsRepository
import com.rodriguesalex.domain.usecase.GetPokeDetailsUseCase
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
        repository: PokeDetailsRepository
    ): GetPokeDetailsUseCase = GetPokeDetailsUseCase(repository = repository)
}
