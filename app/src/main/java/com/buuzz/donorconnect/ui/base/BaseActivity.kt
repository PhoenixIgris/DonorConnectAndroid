package com.buuzz.donorconnect.ui.base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.buuzz.donorconnect.utils.apihelper.InternetConnectionCallback
import com.buuzz.donorconnect.utils.apihelper.InternetConnectionChecker

private const val TAG = "BaseActivity"

open class BaseActivity : AppCompatActivity(), InternetConnectionCallback {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        InternetConnectionChecker
            .instance(this)
            .setCallback(this)
            .register()
    }

    override fun onConnected() {
        Log.d(TAG, "onConnected: ")
    }

    override fun onDisconnected() {
        Log.d(TAG, "onDisconnected: ")
    }

}