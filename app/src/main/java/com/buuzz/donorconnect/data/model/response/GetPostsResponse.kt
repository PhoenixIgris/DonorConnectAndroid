package com.buuzz.donorconnect.data.model.response

data class GetPostResponse(
    val message: String? = null,
    val posts: List<Post>? = emptyList(),
    val success: Boolean? = null
)

data class Post(
    val category_id: Int,
    val comment_list: Any? = null,
    val created_at: String,
    val desc: String? = null,
    val id: Int?,
    val image: String? = null,
    val likes: Int? = null,
    val no_of_comments: Int? = null,
    val queue_id: Any? = null,
    val tag_id: List<Int>? = null,
    val title: String? = null,
    val updated_at: String? = null,
    val user_id: String? = null,
    val user_image: String? = null
)