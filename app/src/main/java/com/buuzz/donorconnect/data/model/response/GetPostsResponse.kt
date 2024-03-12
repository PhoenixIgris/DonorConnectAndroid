package com.buuzz.donorconnect.data.model.response

data class GetPostsResponse(
    val message: String? = null,
    val posts: List<Post>? = emptyList(),
    val success: Boolean? = null
)

data class Post(
    val category_id: Int,
    val comment_list: List<String>? = null,
    val created_at: String,
    val desc: String? = null,
    val id: Int?,
    val image: String? = null,
    val likes: Int? = null,
    val tags: List<Tag>? = null,
    val title: String? = null,
    val updated_at: String? = null,
    val category: Category? = null,
    val user: UserDetails? = null,
    val status: String? = null,
    val pending_request_status: String? = null,
    val current_request_user_id: Int? = null,
    val queue_code: String? = null,
    val request_queues: List<RequestQueue>? = emptyList(),
)

data class RequestQueue(
    val id: Int,
    val user_id: Int,
    val post_id: Int,
    val position: Int

)