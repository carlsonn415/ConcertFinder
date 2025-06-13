package com.example.concertfinder.data.local.entities.classification_entities

import androidx.room.Embedded
import androidx.room.Relation

data class GenreWithSubgenres(
    @Embedded val genre: GenreEntity,
    @Relation(
        parentColumn = "genreId",
        entityColumn = "parentGenreId",
    )
    val subgenres: List<SubgenreEntity>
)