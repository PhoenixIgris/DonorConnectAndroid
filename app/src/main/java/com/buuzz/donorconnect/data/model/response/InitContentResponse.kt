package com.buuzz.donorconnect.data.model.response

data class InitContentResponse(
    val `data`: InitContentData? = null,
    val message: String,
    val success: Boolean,
)

data class InitContentData(
    val categories: List<Category>? = emptyList(),
    val tags: List<Tag>? = emptyList(),
)

data class Category(
    val id: Int,
    val name: String,
    val slug: String,
)


data class Tag(
    val description: String? = null,
    val id: Int,
    val name: String,
    val slug: String,
)