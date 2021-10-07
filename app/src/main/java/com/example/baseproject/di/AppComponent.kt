package com.example.baseproject.di

import android.content.Context
import com.example.baseproject.domain.repository.AppRepository
import com.example.baseproject.domain.usecase.AppUseCase
import com.example.baseproject.presenter.login.TenantLoginActivity
import com.example.baseproject.presenter.recognize.RecognizeActivity
import com.example.baseproject.presenter.register.RegisterActivity
import com.example.baseproject.presenter.splash.SplashScreenActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [RepositoryModule::class, UseCaseModule::class, ViewModelModule::class]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun provideRepository(): AppRepository

    fun provideUseCase(): AppUseCase

    fun inject(activity: SplashScreenActivity)

    fun inject(activity: TenantLoginActivity)

    fun inject(activity: RegisterActivity)

    fun inject(activity: RecognizeActivity)
}