package com.example.lineup_app.data.local.entities.classification_entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "segments")
data class SegmentEntity(
    @PrimaryKey val segmentId: String,
    val name: String,
)
