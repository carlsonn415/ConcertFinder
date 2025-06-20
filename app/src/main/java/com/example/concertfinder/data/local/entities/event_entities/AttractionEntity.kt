package com.example.concertfinder.data.local.entities.event_entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attractions")
data class AttractionEntity(
    @PrimaryKey val attractionId: String,
    val name: String?,
    val description: String?,
    val url: String?,
    val additionalInfo: String?,
)