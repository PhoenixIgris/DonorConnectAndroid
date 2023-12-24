package com.buuzz.donorconnect.data.model.response

data class UserDetailResponse(
    val address: Any,
    val created_at: String,
    val email: String,
    val email_verified_at: Any,
    val first_name: String,
    val fmc_token: Any,
    val id: Int,
    val last_name: String,
    val name: String,
    val phone_number: String,
    val updated_at: String
)