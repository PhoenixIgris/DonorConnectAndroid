package com.buuzz.donorconnect.utils.helpers

import com.buuzz.donorconnect.BuildConfig
import timber.log.Timber


object AppLogger {
    val TAG = "DonorConnect Log ==>"

    fun log(print: String?) {
        if (BuildConfig.DEBUG) AppLogger.logD(TAG, "${print ?: ""}")
    }

    fun logI(tag: String?, message: String?) {
        Timber.tag(tag).i(message)
    }

    fun logW(Tag: String?, message: String?) {
        Timber.tag(Tag).w(message)
    }

    fun logD(Tag: String?, message: String?) {
        Timber.tag(Tag).d(message)
    }

    fun logE(Tag: String?, message: String?) {
        Timber.tag(Tag).e(message)
    }

}