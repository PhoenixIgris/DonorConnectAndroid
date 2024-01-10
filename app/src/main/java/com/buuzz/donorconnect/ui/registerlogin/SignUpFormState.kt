package com.buuzz.donorconnect.ui.registerlogin

import androidx.annotation.Keep

@Keep
data class SignUpFormState(
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val phoneNo: String? = null,
    val password: String? = null,
    val confirmPassword: String? = null,
    val address: String? = null,


    val isFirstNameValid: Boolean = false,
    val isLastNameValid: Boolean = false,
    val isEmailValid: Boolean = false,
    val isPhoneNumberValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isConfirmPasswordValid: Boolean = false,
    val isDataValid: Boolean = false,

    val isFirstNameValidErrMsg: String = "Name is Required",
    val isLastNameValidErrMsg: String = "Name is Required",
    val isEmailValidErrMsg: String? = "Email is Required",
    val isPhoneNumberValidErrMsg: String = "Phone no is Required",
    val isPasswordValidErrMsg: String = "Password is Required",
    val isConfirmPasswordValidErrMsg: String = "Confirm Password is not Same",
)