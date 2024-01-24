package com.buuzz.donorconnect.utils.apihelper

import android.util.Log
import com.buuzz.donorconnect.utils.helpers.AppLogger
import java.io.IOException
import java.net.InetSocketAddress
import javax.net.SocketFactory

object DoesNetworkHaveInternet {
    val TAG = this.javaClass.name

    // Make sure to execute this on a background thread.
    fun execute(socketFactory: SocketFactory): Boolean {
        return try {
            val socket = socketFactory.createSocket() ?: throw IOException("Socket is null.")
            socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            socket.close()
            AppLogger.logD(TAG, "PING success.")
            true
        } catch (e: IOException) {
            AppLogger.logD(TAG, "No internet connection.")
            false
        }
    }
}