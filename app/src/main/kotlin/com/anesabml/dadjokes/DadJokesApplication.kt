package com.anesabml.dadjokes

import android.app.Application
import com.anesabml.dadjokes.di.AppContainer
import timber.log.Timber

class DadJokesApplication : Application() {

    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()

        appContainer = AppContainer(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}