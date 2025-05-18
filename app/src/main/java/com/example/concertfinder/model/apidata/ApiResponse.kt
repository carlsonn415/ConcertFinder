package com.example.concertfinder.model.apidata

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    /*
    UNCOMMENT TO PARSE LINKS AND PAGE FIELDS

    @SerializedName("_links") val links: JsonObject? = null,
    @SerializedName("_page") val page: JsonObject? = null,
    */
    @SerializedName("_embedded") val embedded: EmbeddedApiData? = null,
)

@Serializable
data class EmbeddedApiData(
    @SerializedName("events") val events: List<Event>? = null
)