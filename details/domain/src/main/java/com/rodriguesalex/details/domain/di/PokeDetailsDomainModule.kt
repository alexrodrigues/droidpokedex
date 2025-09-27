package com.rodriguesalex.details.domain.di

import com.rodriguesalex.details.domain.repository.PokeDetailsRepository
import com.rodriguesalex.details.domain.usecase.GetPokeDetailsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PokeDetailsDomainModule {
    @Provides
    @Singleton
    fun providesGetPokeDetailsUseCase(repository: PokeDetailsRepository): GetPokeDetailsUseCase =
        GetPokeDetailsUseCase(repository = repository)
}
