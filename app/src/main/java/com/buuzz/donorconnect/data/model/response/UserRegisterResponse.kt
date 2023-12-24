package com.buuzz.donorconnect.data.model.response

data class UserRegisterResponse(
    val `data`: RegisterDataModel? = null,
    val success: Boolean? = null,
    val message: String? = null
)

data class UserDetails(
    val created_at: String? = null,
    val email: String? = null,
    val first_name: String? = null,
    val fmc_token: String? = null,
    val id: Int? = null,
    val last_name: String? = null,
    val name: String? = null,
    val phone_number: String? = null,
    val updated_at: String? = null,
    val address: String? = null
)

data class Token(
    val token: String? = null
)

data class RegisterDataModel(
    val message: String? = null,
    val token: Token? = null,
    val user_details: UserDetails? = null
)
