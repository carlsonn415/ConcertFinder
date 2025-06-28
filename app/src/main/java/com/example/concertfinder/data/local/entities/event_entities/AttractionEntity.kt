package com.example.concertfinder.data.local.entities.event_entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "attractions",
    indices = [Index("attractionId")]
)
data class AttractionEntity(
    @PrimaryKey val attractionId: String,
    val name: String?,
    val description: String?,
    val url: String?,
    val additionalInfo: String?,
)