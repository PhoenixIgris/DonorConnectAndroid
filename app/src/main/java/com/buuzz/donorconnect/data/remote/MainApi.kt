package com.buuzz.donorconnect.data.remote

import com.buuzz.donorconnect.data.model.request.UserRegisterRequestModel
import com.buuzz.donorconnect.data.model.response.ResponseModel
import com.buuzz.donorconnect.data.model.response.UserDetailResponse
import com.buuzz.donorconnect.data.model.response.UserLoginResponse
import com.buuzz.donorconnect.data.model.response.UserRegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface MainApi {


    @POST(ApiEndPoints.REGISTER)
    suspend fun register(
        @Body data: UserRegisterRequestModel
    ): Response<UserRegisterResponse>

    @FormUrlEncoded
    @POST(ApiEndPoints.LOGIN)
    suspend fun login(
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Response<UserLoginResponse>

    @GET(ApiEndPoints.USER_DETAILS)
    suspend fun fetchUserDetails(): Response<UserDetailResponse>
}