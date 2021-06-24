package com.citrus.ktordemo

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class DrawingApplication: Application() {

    /**github commit test*/
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}