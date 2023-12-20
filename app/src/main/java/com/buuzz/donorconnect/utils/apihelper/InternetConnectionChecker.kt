package com.buuzz.donorconnect.utils.apihelper

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "InternetConnectionCheck"

object InternetConnectionChecker {


    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private var cm: ConnectivityManager? = null
    private val validNetworks: MutableSet<Network> = HashSet()
    private var connectionCallback: InternetConnectionCallback? = null

    fun instance(context: Context): InternetConnectionChecker {
        cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return this
    }

    fun setCallback(connectionCallback: InternetConnectionCallback): InternetConnectionChecker {
        InternetConnectionChecker.connectionCallback = connectionCallback
        return this
    }



    @JvmStatic
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.d(TAG, "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.d(TAG, "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.d(TAG, "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }
    private fun createNetworkCallback() = object : ConnectivityManager.NetworkCallback() {
        /*
          Called when a network is detected. If that network has internet, save it in the Set.
          Source: https://developer.android.com/reference/android/net/ConnectivityManager.NetworkCallback#onAvailable(android.net.Network)
         */
        override fun onAvailable(network: Network) {
            val networkCapabilities = cm?.getNetworkCapabilities(network)
            val hasInternetCapability =
                networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            if (hasInternetCapability == true) {
                // check if this network actually has internet
                CoroutineScope(Dispatchers.IO).launch {
                    val hasInternet = DoesNetworkHaveInternet.execute(network.socketFactory)
                    if (hasInternet) {
                        withContext(Dispatchers.Main) {
                            validNetworks.add(network)
                            checkValidNetworks()
                        }
                    }
                }
            }
        }

        /*
          If the callback was registered with registerNetworkCallback() it will be called for each network which no longer satisfies the criteria of the callback.
          Source: https://developer.android.com/reference/android/net/ConnectivityManager.NetworkCallback#onLost(android.net.Network)
         */
        override fun onLost(network: Network) {
            validNetworks.remove(network)
            checkValidNetworks()
        }

    }

    private fun checkValidNetworks() {
        var status = validNetworks.size > 0
        if (status) {
            connectionCallback?.onConnected()
        } else {
            connectionCallback?.onDisconnected()
        }
    }

    fun register(): InternetConnectionChecker {
        networkCallback = createNetworkCallback()
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        cm?.registerNetworkCallback(networkRequest, networkCallback)
        return this
    }

    fun unRegister() {
        cm?.unregisterNetworkCallback(networkCallback)
    }
}