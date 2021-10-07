package com.example.baseproject.presenter

import android.app.Application
import com.example.baseproject.BuildConfig
import com.example.baseproject.di.AppComponent
import com.example.baseproject.di.DaggerAppComponent
import timber.log.Timber

class MainApplication : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}