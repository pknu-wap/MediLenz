package com.android.mediproject.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Handler
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InternetNetworkListener @Inject constructor(@ApplicationContext private val context: Context) : LifecycleEventObserver {

    private val connectivityManager by lazy {
        context.getSystemService(ConnectivityManager::class.java)
    }

    private val networkCallback: ConnectivityManager.NetworkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            networkStateCallback?.onChangedState(true)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            networkStateCallback?.onChangedState(false)
        }

        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities)
        }

        override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
            super.onLinkPropertiesChanged(network, linkProperties)
        }
    }

    fun interface NetworkStateCallback {
        fun onChangedState(isConnected: Boolean)
    }

    var networkStateCallback: NetworkStateCallback? = null
        set(value) {
            field = value
            connectivityManager.registerDefaultNetworkCallback(networkCallback,
                Handler.createAsync(context.mainLooper))
        }

    var activityLifeCycle: Lifecycle? = null
        set(value) {
            field = value
            field?.addObserver(this)
        }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_START -> {
                connectivityManager.unregisterNetworkCallback(networkCallback)
            }

            Lifecycle.Event.ON_PAUSE -> {
                networkStateCallback?.onChangedState(false)
            }

            else -> {}
        }
    }
}