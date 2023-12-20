package com.buuzz.donorconnect.data.respository

import android.util.Patterns
import com.buuzz.donorconnect.data.model.request.UserRegisterRequestModel
import com.buuzz.donorconnect.data.remote.MainApi
import com.buuzz.donorconnect.ui.registerlogin.SignUpFormState
import com.buuzz.donorconnect.ui.registerlogin.SignUpValidationEvent
import com.buuzz.donorconnect.utils.apihelper.safeapicall.Resource
import com.buuzz.donorconnect.utils.apihelper.safeapicall.SafeApiCall
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject


class UserRepository @Inject constructor(
    private val mainApi: MainApi
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
                signUpValidationEventChannel.send(SignUpValidationEvent.Success(response.value))
            }
        }
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


}