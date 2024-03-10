package com.buuzz.donorconnect.data.model.request

data class UserRegisterRequestModel(
    val first_name: String? = null,
    val last_name: String? = null,
    val email: String? = null,
    val password: String? = null,
    val fmc_token: String? = "s",
    val phone_number: String? = null,
    val address: String? = null,
    val name : String? = null
)