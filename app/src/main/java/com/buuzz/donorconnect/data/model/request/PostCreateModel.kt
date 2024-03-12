package com.buuzz.donorconnect.data.model.request

data class PostCreateModel(
    val image: String?,
    val title: String,
    val desc: String,
    val category_id: Int?,
    val tag_id: List<Int>?,
    val user_id: String?,
    val address : String,
    val latitude: Double,
    val longitude : Double
)