package com.rodriguesalex.data.di

import com.rodriguesalex.data.repository.PokeHomeRepositoryImpl
import com.rodriguesalex.data.service.PokeHomeService
import com.rodriguesalex.domain.repository.PokeHomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PokeHomeDataModule {
    @Provides
    @Singleton
    fun provideCharacterService(retrofit: Retrofit) = retrofit.create(PokeHomeService::class.java)

    @Provides
    @Singleton
    fun providePokeHomeRepository(service: PokeHomeService): PokeHomeRepository = PokeHomeRepositoryImpl(service)
}
