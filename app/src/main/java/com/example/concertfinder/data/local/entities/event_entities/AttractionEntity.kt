package com.example.concertfinder.data.local.entities.event_entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "attractions",
    foreignKeys = [
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = ["eventId"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("eventId")]
)
data class AttractionEntity(
    @PrimaryKey val attractionId: String,
    val eventId: String?,
    val name: String?,
    val description: String?,
    val url: String?,
    val additionalInfo: String?,
)