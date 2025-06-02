package com.example.concertfinder.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.example.concertfinder.data.local.entities.GenreEntity
import com.example.concertfinder.data.local.entities.SegmentEntity
import com.example.concertfinder.data.local.entities.SubgenreEntity

data class SegmentWithGenres(
    @Embedded val segment: SegmentEntity,
    @Relation(
        parentColumn = "segmentId",
        entityColumn = "parentSegmentId",
    )
    val genres: List<GenreWithSubgenres>
)

data class GenreWithSubgenres(
    @Embedded val genre: GenreEntity,
    @Relation(
        parentColumn = "genreId",
        entityColumn = "parentGenreId",
    )
    val subgenres: List<SubgenreEntity>
)