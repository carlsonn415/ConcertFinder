package com.example.concertfinder.data.local.entities.event_entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "events",
    indices = [Index("eventId")]
)
data class EventEntity(

    @PrimaryKey val eventId: String, // Unique identifier for the event

    val name: String?, // Name of the event

    @Embedded val location: LocationDataEntity?, // Latitude and longitude

    val url: String?, // URL for more information about the event

    val description: String?, // Description of the event

    val additionalInfo: String?, // Additional information about the event

    @Embedded val dates: DateDataEntity?, // Date and time information for the event

    val info: String?, // Information about the event

    val pleaseNote: String?, // Information to be displayed to the user

    @Embedded val priceRanges: PriceRangeEntity?, // List of price ranges for the event

    @Embedded val place: PlaceEntity?, // Place information for the event, ONLY USE IF NO VENUES ARE PROVIDED
)