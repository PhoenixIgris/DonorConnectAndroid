package com.buuzz.donorconnect.data.local


import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.buuzz.donorconnect.data.local.SharedPreferencesConstants.ACCESS_TOKEN
import com.buuzz.donorconnect.data.local.SharedPreferencesConstants.PREF_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@SuppressLint("CommitPrefEdits") //Showing .apply() is needed even though it is there.
@Singleton
class SharedPreferencesHelper
@Inject
constructor(@ApplicationContext context: Context) {
    private val sharedPref = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE)

    var accessToken: String?
        get() = sharedPref.getString(ACCESS_TOKEN, "")
        set(accessToken) = sharedPref.edit().putString(ACCESS_TOKEN, accessToken).apply()

    var password: String?
        get() = sharedPref.getString(SharedPreferencesConstants.PASSWORD, "")
        set(password) = sharedPref.edit().putString(SharedPreferencesConstants.PASSWORD, password)
            .apply()

}

object SharedPreferencesConstants {
    const val PREF_NAME = "com.buuzz.donorconnect.preferences"
    const val ACCESS_TOKEN = "accessToken"
    const val PASSWORD = "password"

}