package com.android.mediproject.core.network

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.os.Handler
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NetworkStatusManager @Inject constructor(@ApplicationContext private val context: Context) : LifecycleEventObserver {

    private val connectivityManager =
        context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager


    private val networkCallback: ConnectivityManager.NetworkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            networkStateCallback?.onChangedState(true)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            networkStateCallback?.onChangedState(false)
        }

    }

    fun interface NetworkStateCallback {
        fun onChangedState(isConnected: Boolean)
    }

    var networkStateCallback: NetworkStateCallback? = null
        set(value) {
            field = value
            connectivityManager.registerDefaultNetworkCallback(networkCallback, Handler.createAsync(context.mainLooper))
        }

    var activityLifeCycle: Lifecycle? = null
        set(value) {
            field = value
            field?.addObserver(this)
        }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_START -> {

            }

            Lifecycle.Event.ON_PAUSE -> {

            }

            else -> {}
        }
    }
}
