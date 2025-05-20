package com.example.concertfinder.data.remote.event_dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class EventImage(
    @SerializedName("url") val url: String? = null,
    @SerializedName("ratio") val ratio: String? = null
)