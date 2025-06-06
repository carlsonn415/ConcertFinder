package com.example.concertfinder.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "segments")
data class SegmentEntity(
    @PrimaryKey val segmentId: String,
    val name: String,
)
