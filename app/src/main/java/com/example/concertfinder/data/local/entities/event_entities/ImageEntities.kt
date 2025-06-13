package com.example.concertfinder.data.local.entities.event_entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.concertfinder.data.remote.event_dto.EventImage

@Entity(
    tableName = "event_image_entities",
    foreignKeys = [
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = ["eventId"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class EventImageEntity(
    @PrimaryKey(autoGenerate = true) val imageId: Int = 0,
    val eventId: String? = null,
    val url: String?,
    val ratio: String?,
    val width: Int?,
    val height: Int?,
    val fallback: Boolean?
) {
    fun toEventImage(): EventImage {
        return EventImage(
            url = url,
            ratio = ratio,
            width = width,
            height = height,
            fallback = fallback
        )
    }
}

@Entity(
    tableName = "attraction_image_entities",
    foreignKeys = [
        ForeignKey(
            entity = AttractionEntity::class,
            parentColumns = ["attractionId"],
            childColumns = ["attractionId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AttractionImageEntity(
    @PrimaryKey(autoGenerate = true) val imageId: Int = 0,
    val attractionId: String? = null,
    val url: String?,
    val ratio: String?,
    val width: Int?,
    val height: Int?,
    val fallback: Boolean?
) {
    fun toAttractionImage(): EventImage {
        return EventImage(
            url = url,
            ratio = ratio,
            width = width,
            height = height,
            fallback = fallback
        )
    }
}

@Entity(
    tableName = "venue_image_entities",
    foreignKeys = [
        ForeignKey(
            entity = VenueEntity::class,
            parentColumns = ["venueId"],
            childColumns = ["venueId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class VenueImageEntity(
    @PrimaryKey(autoGenerate = true) val imageId: Int = 0,
    val venueId: String? = null,
    val url: String?,
    val ratio: String?,
    val width: Int?,
    val height: Int?,
    val fallback: Boolean?
) {
    fun toVenueImage(): EventImage {
        return EventImage(
            url = url,
            ratio = ratio,
            width = width,
            height = height,
            fallback = fallback
        )
    }
}