package com.example.lineup_app.data.local.entities.event_entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "event_attraction_cross_ref",
    primaryKeys = ["eventId", "attractionId"], // Composite primary key
    foreignKeys = [
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = ["eventId"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AttractionEntity::class,
            parentColumns = ["attractionId"],
            childColumns = ["attractionId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = ["eventId"]),
        Index(value = ["attractionId"])
    ]
)
data class EventAttractionCrossRef(
    val eventId: String,
    val attractionId: String
)
