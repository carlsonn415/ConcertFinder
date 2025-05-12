package com.example.concertfinder.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    /*
    UNCOMMENT TO PARSE LINKS AND PAGE FIELDS

    @SerializedName("_links") val links: JsonObject? = null,
    @SerializedName("_page") val page: JsonObject? = null,
    */
    @SerializedName("_embedded") val embedded: EmbeddedData? = null,
)

@Serializable
data class EmbeddedData(
    @SerializedName("events") val events: List<Event>
)

@Serializable
data class EventImage(
    @SerializedName("url") val url: String,
)

@Serializable
data class DateInfo(
    @SerializedName("start") val start: StartDateInfo,
)

@Serializable
data class StartDateInfo(
    @SerializedName("localDate") val date: String,
    @SerializedName("localTime") val time: String,
)

@Serializable
data class Event(
    @SerializedName("id")
    val id: String, // Unique identifier for the event
    @SerializedName("name")
    val name: String, // Name of the event

    //val artist: String, // Name of the artist or group performing the event

    @SerializedName("location")
    val location: Pair<Double, Double>, // Latitude and longitude

    @SerializedName("images")
    val imageList: List<EventImage>, // List of image URLs for the event

    @SerializedName("dates")
    val dateInfo: DateInfo // Information about the date and time of the event
)
