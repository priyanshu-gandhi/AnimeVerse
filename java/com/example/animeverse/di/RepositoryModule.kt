package com.example.animeverse.di

import com.example.animeverse.data.repoImpl.DetailsRepositoryImpl
import com.example.animeverse.data.repoImpl.HomeRepositoryImpl
import com.example.animeverse.domain.repository.DetailsRepository
import com.example.animeverse.domain.repository.HomeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindHomeRepository(
        impl: HomeRepositoryImpl
    ): HomeRepository

    @Binds
    abstract fun bindDetailsRepository(
        impl: DetailsRepositoryImpl
    ) : DetailsRepository


}