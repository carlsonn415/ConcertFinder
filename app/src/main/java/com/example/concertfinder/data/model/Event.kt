package com.example.concertfinder.data.model

import com.example.concertfinder.data.remote.event_dto.Classification
import com.example.concertfinder.data.remote.event_dto.DateData
import com.example.concertfinder.data.remote.event_dto.EmbeddedEventData
import com.example.concertfinder.data.remote.event_dto.EventImage
import com.example.concertfinder.data.remote.event_dto.LocationData
import com.example.concertfinder.data.remote.event_dto.Place
import com.example.concertfinder.data.remote.event_dto.PriceRange
import kotlinx.serialization.Serializable

@Serializable
data class Event(

    val embedded: EmbeddedEventData? = null, // Embedded data containing venues and attractions

    val id: String? = null, // Unique identifier for the event

    val name: String? = null, // Name of the event

    val location: LocationData? = null, // Latitude and longitude

    val url: String? = null, // URL for more information about the event

    val description: String? = null, // Description of the event

    val additionalInfo: String? = null, // Additional information about the event

    val dates: DateData? = null, // Date and time information for the event

    val images: List<EventImage>? = null, // List of image URLs for the event

    val info: String? = null, // Information about the event

    val pleaseNote: String? = null, // Information to be displayed to the user

    val priceRanges: List<PriceRange>? = null, // List of price ranges for the event

    val classifications: List<Classification>? = null, // List of classifications for the event

    val place: Place? = null, // Place information for the event, ONLY USE IF NO VENUES ARE PROVIDED

    var saved: Boolean = false // Whether the event is saved by the user or not
)