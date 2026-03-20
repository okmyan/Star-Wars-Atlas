package com.okmyan.starwarsatlas.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import com.okmyan.starwarsatlas.BuildConfig
import timber.log.Timber

@HiltAndroidApp
class StarWarsAtlasApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
