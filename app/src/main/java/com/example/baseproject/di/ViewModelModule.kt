package com.example.baseproject.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.baseproject.presenter.login.TenantLoginViewModel
import com.example.baseproject.presenter.recognize.RecognizeViewModel
import com.example.baseproject.presenter.register.RegisterViewModel
import com.example.baseproject.presenter.splash.SplashScreenViewModel
import com.example.baseproject.util.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SplashScreenViewModel::class)
    abstract fun bindSplashScreenViewModel(splashScreenViewModel: SplashScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TenantLoginViewModel::class)
    abstract fun bindTenantLoginViewModel(tenantLoginViewModel: TenantLoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    abstract fun bindRegisterViewModel(registerViewModel: RegisterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RecognizeViewModel::class)
    abstract fun bindRecognizeViewModel(recognizeViewModel: RecognizeViewModel): ViewModel
}