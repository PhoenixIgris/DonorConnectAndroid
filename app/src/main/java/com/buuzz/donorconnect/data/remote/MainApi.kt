package com.buuzz.donorconnect.data.remote

import com.buuzz.donorconnect.data.model.request.UserRegisterRequestModel
import com.buuzz.donorconnect.data.model.response.ResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MainApi {


    @POST(ApiEndPoints.REGISTER)
    suspend fun register(
        @Body data: UserRegisterRequestModel
    ): Response<ResponseModel>

}