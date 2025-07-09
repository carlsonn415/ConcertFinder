package com.example.lineup_app.data.remote.event_dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Classification(
    @SerializedName("segment") val segment: Segment? = null, // Segment of the event (e.g. Music)
    @SerializedName("genre") val genre: Genre? = null, // Genre of the event
    @SerializedName("subGenre") val subGenre: SubGenre? = null,  // Sub-genre of the event
)

@Serializable
data class Segment(
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val name: String? = null,
)

@Serializable
data class Genre(
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val name: String? = null,
)

@Serializable
data class SubGenre(
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val name: String? = null,
)


