package com.example.concertfinder.data.local.entities.classification_entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

@Entity(
    tableName = "genres",
    foreignKeys = [
        ForeignKey(
            entity = SegmentEntity::class,
            parentColumns = ["segmentId"],
            childColumns = ["parentSegmentId"],
            onDelete = ForeignKey.CASCADE // If a segment is deleted, also delete its genres
        )
    ],
    indices = [Index(value = ["parentSegmentId"])] // index for faster lookups
)
data class GenreEntity(
    @PrimaryKey val genreId: String,
    val name: String,
    @ColumnInfo(name = "parentSegmentId")
    val parentSegmentId: String // Foreign key to SegmentEntity
)
