package com.buuzz.donorconnect.data.model.response

data class GetPostResponse(
    val `data`: Data? = null,
    val message: String? = null,
    val success: Boolean? = null
)

data class Data(
    val post_detail: Post? = null
)