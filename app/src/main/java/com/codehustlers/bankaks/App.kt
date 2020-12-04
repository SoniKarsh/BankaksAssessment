package com.codehustlers.bankaks

import android.app.*
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.interceptors.HttpLoggingInterceptor

class App: Application() {

    private lateinit var connectivityManager: ConnectivityManager
    private var networkCallback: ConnectivityManager.NetworkCallback? = null
    var isBackPressEnable = true
    var isNetworkAvailable = false

    override fun onCreate() {
        super.onCreate()

        AndroidNetworking.initialize(applicationContext)
        if (BuildConfig.DEBUG) {
            AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        }

        instance = this
        startReceivingNetworkCallback()

    }

    private fun startReceivingNetworkCallback(){

        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkRequestBuilder = NetworkRequest.Builder()

        networkRequestBuilder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        networkRequestBuilder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        networkRequestBuilder.addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
        networkRequestBuilder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI)

        isNetworkAvailable = connectivityManager.activeNetwork != null

        connectivityManager.requestNetwork(networkRequestBuilder.build(), object : ConnectivityManager.NetworkCallback() {

            override fun onLost(network: Network) {
                isNetworkAvailable = false
//                EventBus.getDefault().post(NetworkAvailableEvent(false))
            }

            override fun onAvailable(network: Network) {
                connectivityManager.bindProcessToNetwork(network)
                isNetworkAvailable = true
//                EventBus.getDefault().post(NetworkAvailableEvent(true))
            }

            override fun onUnavailable() {
                isNetworkAvailable = false
//                EventBus.getDefault().post(NetworkAvailableEvent(false))
            }

        })
    }

    companion object {
        var instance: App? = null
    }

}