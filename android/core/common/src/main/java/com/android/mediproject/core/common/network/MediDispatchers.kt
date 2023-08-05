package com.android.mediproject.core.common.network

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val mediDispatcher: MediDispatchers)

enum class MediDispatchers {
    Default,
    IO,
    Main,
}