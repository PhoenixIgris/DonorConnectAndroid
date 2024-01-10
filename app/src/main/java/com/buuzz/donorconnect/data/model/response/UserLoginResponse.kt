package com.buuzz.donorconnect.data.model.response

data class UserLoginResponse(
    val `data`: LoginModel? = null,
    val message: String? = null,
    val success: Boolean? = null
)

data class LoginModel(
    val user_details: UserDetails? = null,
    val token: String? = null
)