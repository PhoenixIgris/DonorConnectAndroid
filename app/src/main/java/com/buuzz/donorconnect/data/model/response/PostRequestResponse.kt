package com.buuzz.donorconnect.data.model.response

data class PostRequestResponse(
    val delivery_details: DeliveryDetails? = null,
    val isFirstRequest: Boolean? = null,
    val message: String? = null,
    val success: Boolean? = null
)

data class DeliveryDetails(
    val message: String? = null,
    val queue_code: String? = null,
    val address : Address? = null,
    val contact_number : String? = null
)


data class Address(
    val id: Int? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val name: String? = null,
)