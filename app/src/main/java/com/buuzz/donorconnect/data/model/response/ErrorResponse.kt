package com.buuzz.donorconnect.data.model.response

data class ErrorResponse(
    val code: Int,
    val message: String,
    val status: Boolean
)