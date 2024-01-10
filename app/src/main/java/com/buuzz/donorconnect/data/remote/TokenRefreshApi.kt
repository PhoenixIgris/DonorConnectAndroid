package com.buuzz.donorconnect.data.remote

import retrofit2.Response
import retrofit2.http.POST

interface TokenRefreshApi {

    @POST(ApiEndPoints.REFRESH_TOKEN)
    suspend fun refreshToken(): Response<Any>
}