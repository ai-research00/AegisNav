package com.aegisnav

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class AegisNavApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        
        Timber.d("AegisNav Application initialized")
    }
}
