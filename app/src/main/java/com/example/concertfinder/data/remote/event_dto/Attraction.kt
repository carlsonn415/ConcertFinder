package com.example.concertfinder.data.remote.event_dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Attraction(
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("url") val url: String? = null,
    @SerializedName("images") val images: List<EventImage>? = null,
    @SerializedName("additionalInfo") val additionalInfo: String? = null,
    @SerializedName("classifications") val classifications: List<Classification>? = null,
)
