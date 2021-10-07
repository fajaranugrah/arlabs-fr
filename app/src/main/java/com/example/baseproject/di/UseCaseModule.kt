package com.example.baseproject.di

import com.example.baseproject.data.source.AppRepositoryImpl
import com.example.baseproject.domain.repository.AppRepository
import com.example.baseproject.domain.usecase.AppUseCase
import com.example.baseproject.domain.usecase.AppUseCaseImpl
import dagger.Binds
import dagger.Module

@Module(includes = [NetworkModule::class])
abstract class UseCaseModule {

    @Binds
    abstract fun provideUseCase(useCase: AppUseCaseImpl): AppUseCase
}