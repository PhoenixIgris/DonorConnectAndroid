package com.buuzz.donorconnect.di.authenticator

import android.content.Context
import android.util.Log
import com.buuzz.donorconnect.data.local.SharedPreferencesHelper
import com.buuzz.donorconnect.data.remote.TokenRefreshApi
import com.buuzz.donorconnect.utils.apihelper.safeapicall.Resource
import com.buuzz.donorconnect.utils.apihelper.safeapicall.SafeApiCall
import com.buuzz.donorconnect.utils.helpers.AppLogger
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject


private const val TAG = "AccessAuthenticator"

class AccessAuthenticator
@Inject constructor(
    private val sharedPreferencesHelper: SharedPreferencesHelper,
    @ApplicationContext
    private val appContext: Context,
    private val tokenRefreshApi: TokenRefreshApi,
) : Authenticator {


    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            when (val newToken = getRefreshedToken(tokenRefreshApi)) {
                is Resource.Success -> {
                    AppLogger.logD(TAG, "authenticate: Token Refreshed")
                    sharedPreferencesHelper.accessToken = newToken.value.toString()
                    response.request.newBuilder()
                        .addHeader(
                            "Authorization",
                            newToken.value.toString()
                        ).build()
                }

                else -> {
                    AppLogger.logD(TAG, "authenticate: Token Refresh Failed")
                    startTokenExpiryHandlerActivity()
                    null
                }
            }
        }
    }

    private fun startTokenExpiryHandlerActivity() {
//        val intent = Intent(appContext, TokenExpiryHandlerActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        appContext.startActivity(intent)
    }

    private suspend fun getRefreshedToken(tokenRefreshApi: TokenRefreshApi): Resource<Any> {
        return SafeApiCall.execute { tokenRefreshApi.refreshToken() }
    }


}