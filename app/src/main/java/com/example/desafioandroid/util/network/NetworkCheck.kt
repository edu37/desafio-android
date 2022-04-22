package com.example.desafioandroid.util.network

import android.hardware.camera2.params.Capability
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build

class NetworkCheck(
    private val connectivityManager: ConnectivityManager?
) {

    fun hasConnection(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network: Network = connectivityManager?.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
        } else {
            val activeNetworkInfo = connectivityManager?.activeNetworkInfo ?: return false

            activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI ||
                    activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE
        }
    }
}