package com.rodriguesalex.droidpokedex.network.services.di

import com.rodriguesalex.droidpokedex.network.services.PokeHomeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object PokeServicesModule {
    @Provides
    @Singleton
    fun provideCharacterService(retrofit: Retrofit): PokeHomeService =
        retrofit.create(PokeHomeService::class.java)
}