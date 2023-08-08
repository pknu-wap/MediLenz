package com.android.mediproject

import android.app.Application
import com.android.mediproject.core.model.common.UiModelMappingRegister
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MediApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        UiModelMappingRegister.register()
    }
}
