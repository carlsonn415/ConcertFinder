package com.example.concertfinder.data.local.entities.classification_entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "subgenres",
    foreignKeys = [
        ForeignKey(
            entity = GenreEntity::class,
            parentColumns = ["genreId"],
            childColumns = ["parentGenreId"],
            onDelete = ForeignKey.CASCADE // If a genre is deleted, also delete its subgenres
        )
    ],
    indices = [Index(value = ["parentGenreId"])] // index for faster lookups
)
data class SubgenreEntity(
    @PrimaryKey val subgenreId: String,
    val name: String,
    @ColumnInfo(name = "parentGenreId")
    val parentGenreId: String // Foreign key to GenreEntity
)
