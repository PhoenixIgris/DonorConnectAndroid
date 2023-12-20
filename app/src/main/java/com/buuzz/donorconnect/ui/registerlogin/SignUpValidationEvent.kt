package com.buuzz.donorconnect.ui.registerlogin

import androidx.annotation.Keep
import com.buuzz.donorconnect.data.model.response.ResponseModel

sealed class SignUpValidationEvent {
    @Keep
    data class Success(val response: ResponseModel?) : SignUpValidationEvent()

    @Keep
    data class Failure(val errorMessage: String) : SignUpValidationEvent()

    @Keep
    data class Loading(val message: String) : SignUpValidationEvent()
    object UpdateData : SignUpValidationEvent()
    object ShowValidationError : SignUpValidationEvent()
}