package com.buuzz.donorconnect.data.model.response

data class UserRequestListResponse(
    val message: String? = null,
    val success: Boolean? = null,
    val user_requests: List<UserRequest?>? = null
)

data class UserRequest(
    val created_at: String? = null,
    val id: Int? = null,
    val position: Int? = null,
    val post: Post? = null,
    val post_id: Int? = null,
    val queue_code: String? = null,
    val updated_at: String? = null,
    val user_id: Int? = null
)