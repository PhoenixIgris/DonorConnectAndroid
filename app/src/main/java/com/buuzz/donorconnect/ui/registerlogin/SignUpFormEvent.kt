package com.buuzz.donorconnect.ui.registerlogin

import androidx.annotation.Keep

sealed class SignUpFormEvent {
    @Keep
    data class FirstNameChanged(val name: String?) : SignUpFormEvent()

    @Keep
    data class LastNameChanged(val name: String?) : SignUpFormEvent()

    @Keep
    data class EmailChanged(val email: String?) : SignUpFormEvent()

    @Keep
    data class PhoneNoChanged(val phone: String?) : SignUpFormEvent()

    @Keep
    data class PasswordChanged(val password: String?) : SignUpFormEvent()

    @Keep
    data class ConfirmPasswordChanged(val password: String?, val confirmPassword: String?) :
        SignUpFormEvent()

    @Keep
    data class AddressChanged(val address: String?) : SignUpFormEvent()

    object Submit : SignUpFormEvent()
}