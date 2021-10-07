package com.example.baseproject.di

import com.example.baseproject.data.source.AppRepositoryImpl
import com.example.baseproject.domain.repository.AppRepository
import dagger.Binds
import dagger.Module

@Module(includes = [NetworkModule::class, DatabaseModule::class])
abstract class RepositoryModule {

    @Binds
    abstract fun provideRepository(repository: AppRepositoryImpl): AppRepository
}