package com.example.lineup_app.data.local.entities.event_entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "event_venue_cross_ref",
    primaryKeys = ["eventId", "venueId"], // Composite primary key
    foreignKeys = [
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = ["eventId"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = VenueEntity::class,
            parentColumns = ["venueId"],
            childColumns = ["venueId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = ["eventId"]),
        Index(value = ["venueId"])
    ]
)
data class EventVenueCrossRef(
    val eventId: String,
    val venueId: String
)
