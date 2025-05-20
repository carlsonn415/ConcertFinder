package com.example.concertfinder.data.remote.event_dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class EventsApiResponse(
    /*
    UNCOMMENT TO PARSE LINKS AND PAGE FIELDS

    @SerializedName("_links") val links: JsonObject? = null,
    @SerializedName("_page") val page: JsonObject? = null,
    */
    @SerializedName("_embedded") val embedded: EmbeddedApiData? = null,
)

@Serializable
data class EmbeddedApiData(
    @SerializedName("events") val events: List<EventDto>? = null
)