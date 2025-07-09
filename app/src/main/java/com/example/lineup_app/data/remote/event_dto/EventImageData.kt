package com.example.lineup_app.data.remote.event_dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class EventImage(
    @SerializedName("url") val url: String? = null,
    @SerializedName("ratio") val ratio: String? = null,
    @SerializedName("width") val width: Int? = null,
    @SerializedName("height") val height: Int? = null,
    @SerializedName("fallback") val fallback: Boolean? = null

)