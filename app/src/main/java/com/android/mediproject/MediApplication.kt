package com.android.mediproject

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MediApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}