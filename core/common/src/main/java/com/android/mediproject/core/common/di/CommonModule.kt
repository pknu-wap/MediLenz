package com.android.mediproject.core.common.di

import com.android.mediproject.core.common.uiutil.LayoutController
import com.android.mediproject.core.common.uiutil.SystemBarController
import com.android.mediproject.core.common.uiutil.SystemBarStyler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonModule {

    @Singleton
    @Provides
    fun providesSystemBarStyler() = SystemBarStyler()

    @Singleton
    @Provides
    fun providesLayoutController(systemBarStyler: SystemBarStyler): LayoutController = systemBarStyler

    @Singleton
    @Provides
    fun providesSystemBarController(systemBarStyler: SystemBarStyler): SystemBarController = systemBarStyler
}
