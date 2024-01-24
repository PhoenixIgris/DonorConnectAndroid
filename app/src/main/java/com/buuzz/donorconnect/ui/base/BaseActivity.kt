package com.buuzz.donorconnect.ui.base

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.buuzz.donorconnect.ui.custom.NoInternetActivity
import com.buuzz.donorconnect.utils.apihelper.InternetConnectionCallback
import com.buuzz.donorconnect.utils.apihelper.InternetConnectionChecker
import com.buuzz.donorconnect.utils.helpers.AppLogger
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

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
        AppLogger.logD(TAG, "onConnected: ")
    }

    override fun onDisconnected() {
        startActivity(Intent(this, NoInternetActivity::class.java))
    }

    fun showTopSnackBar(view: View, message: String) {
        val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        val params = snackBarView.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        snackBarView.layoutParams = params
        snackBar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
        snackBar.show()
    }


}