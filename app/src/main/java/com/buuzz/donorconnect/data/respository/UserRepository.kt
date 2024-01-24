package com.buuzz.donorconnect.data.respository

import android.util.Patterns
import com.buuzz.donorconnect.data.local.DataStoreHelper
import com.buuzz.donorconnect.data.local.SharedPreferencesHelper
import com.buuzz.donorconnect.data.model.request.UserRegisterRequestModel
import com.buuzz.donorconnect.data.model.response.RegisterDataModel
import com.buuzz.donorconnect.data.remote.MainApi
import com.buuzz.donorconnect.ui.registerlogin.SignUpFormState
import com.buuzz.donorconnect.ui.registerlogin.SignUpValidationEvent
import com.buuzz.donorconnect.utils.apihelper.safeapicall.ApiCallListener
import com.buuzz.donorconnect.utils.apihelper.safeapicall.Resource
import com.buuzz.donorconnect.utils.apihelper.safeapicall.SafeApiCall
import com.buuzz.donorconnect.utils.helpers.AppData
import com.google.gson.Gson
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject


class UserRepository @Inject constructor(
    private val mainApi: MainApi,
    private val dataStoreHelper: DataStoreHelper,
    private val sharedPreferencesHelper: SharedPreferencesHelper
) {


    suspend fun registerUser(
        data: UserRegisterRequestModel,
        signUpValidationEventChannel: Channel<SignUpValidationEvent>
    ) {
        when (val response = SafeApiCall.execute { mainApi.register(data) }) {
            is Resource.Failure -> {
                signUpValidationEventChannel.send(
                    SignUpValidationEvent.Failure(
                        response.errorMsg ?: "server error"
                    )
                )
            }

            is Resource.Success -> {
                if (response.value.success == true) {
                    signUpValidationEventChannel.send(SignUpValidationEvent.Success(response.value.message))
                    saveUserDetails(response.value.data)
                } else {
                    signUpValidationEventChannel.send(
                        SignUpValidationEvent.Failure(
                            response.value.message ?: "Error Signing up"
                        )
                    )
                }
            }
        }
    }

    private fun saveUserDetails(data: RegisterDataModel?) {


    }


    internal fun validateEmail(email: String?): Pair<Boolean, String> {
        if (email.isNullOrEmpty()) {
            return false to "Email is is Required"
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false to "Invalid Email"
        }
        return true to ""
    }

    internal fun validatePhoneNumber(phone: String?): Pair<Boolean, String> {
        if (phone.isNullOrEmpty()) {
            return false to "Phone Number is Required"
        }
        return true to ""
    }


    internal fun validateData(
        signUpState: SignUpFormState,
        function: (isDataValid: Boolean) -> Unit
    ) {

        val value =
            signUpState.isEmailValid && signUpState.isFirstNameValid && signUpState.isLastNameValid
        function.invoke(value)
    }


    internal fun validateName(name: String?): Pair<Boolean, String> {
        if (name.isNullOrEmpty()) {
            return false to "Name is is Required"
        }
        return true to ""
    }

    fun validatePassword(password: String?): Pair<Boolean, String> {
        if (password.isNullOrEmpty()) {
            return false to "Password is is Required"
        }
        return true to ""
    }

    fun validateConfirmPassword(
        password: String?,
        confirmPassword: String?
    ): Pair<Boolean, String> {
        if (confirmPassword != password) {
            return false to "Confirm Password is not Equal to Password."
        }
        return true to ""
    }

    suspend fun loginUser(email: String?, password: String?, callback: ApiCallListener) {
        when (val response = SafeApiCall.execute { mainApi.login(email, password) }) {
            is Resource.Failure -> {
                callback.onError(response.errorMsg)
            }

            is Resource.Success -> {
                if (response.value.success == true) {
                    sharedPreferencesHelper.accessToken = response.value.data?.token
                    dataStoreHelper.saveStringToDatastore(
                        AppData.USER_DETAILS to Gson().toJson(
                            response.value.data?.user_details
                        )
                    )
                    dataStoreHelper.saveStringToDatastore(AppData.USER_ID to response.value.data?.user_details?.id.toString())
                    dataStoreHelper.saveBooleanToDatastore(AppData.IS_USER_LOGGED_IN to true)
                    callback.onSuccess(response.value.message)
                } else {
                    callback.onError(response.value.message)
                }

            }
        }
    }

    suspend fun fetchUserDetails() {
        when (val response = SafeApiCall.execute { mainApi.fetchUserDetails() }) {
            is Resource.Failure -> {}
            is Resource.Success -> {
                dataStoreHelper.saveStringToDatastore(
                    AppData.USER_DETAILS to Gson().toJson(
                        response.value
                    )
                )
                dataStoreHelper.saveStringToDatastore(AppData.USER_ID to response.value.id.toString())

            }
        }
    }

}