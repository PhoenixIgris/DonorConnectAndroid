package com.buuzz.donorconnect.utils.apihelper.safeapicall

import android.util.Log
import com.buuzz.donorconnect.data.model.response.ErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

private const val TAG = "SafeApiCall"

object SafeApiCall {
    suspend fun <T> execute(
        apiCall: suspend () -> Response<T>
    ): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiCall.invoke()
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        Resource.Success(response.body()!!)
                    } else {
                        Resource.Failure("Something went wrong.", true)
                    }
                } else {
                    if (response.code() == 401) {
                        Log.e(TAG, "UnAuthorized Request")
                        Resource.Failure(response.message() ?: "UnAuthorized Request", false)
                    } else {
                        val gson = Gson()
                        val errorResponse =
                            gson.fromJson(
                                response.errorBody()?.charStream(),
                                ErrorResponse::class.java
                            )
                        Resource.Failure(errorResponse.message, false)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Unknown Api Error: ${e.message}")
                Resource.Failure("Something went wrong", true)
            }
        }
    }
}