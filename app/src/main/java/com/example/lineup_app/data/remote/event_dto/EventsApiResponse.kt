package com.example.lineup_app.data.remote.event_dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class EventsApiResponse(
    /*
    UNCOMMENT TO PARSE LINKS FIELD

    @SerializedName("_links") val links: JsonObject? = null,
    */
    @SerializedName("page") val pageData: PageApiData? = null,

    @SerializedName("_embedded") val embedded: EmbeddedApiData? = null,
)

@Serializable
data class EmbeddedApiData(
    @SerializedName("events") val events: List<EventDto>? = null
)

@Serializable
data class PageApiData(
    @SerializedName("size") val pageSize: Int? = null,
    @SerializedName("totalElements") val totalEvents: Int? = null,
    @SerializedName("totalPages") val totalPages: Int? = null,
    @SerializedName("number") val pageNumber: Int? = null,
)