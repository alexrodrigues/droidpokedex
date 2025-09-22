package com.rodriguesalex.details.data.di

import com.rodriguesalex.details.data.repository.PokeDetailsRepositoryImpl
import com.rodriguesalex.details.data.service.PokeDetailsService
import com.rodriguesalex.details.domain.repository.PokeDetailsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PokeDetailsDataModule {
    @Provides
    @Singleton
    fun provideCharacterService(retrofit: Retrofit) = retrofit.create(PokeDetailsService::class.java)

    @Provides
    @Singleton
    fun providePokeHomeRepository(service: PokeDetailsService): PokeDetailsRepository =
        PokeDetailsRepositoryImpl(service)
}
