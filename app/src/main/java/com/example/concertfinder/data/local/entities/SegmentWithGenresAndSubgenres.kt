package com.example.concertfinder.data.local.entities

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
        entity = SubgenreEntity::class,
        parentColumn = "genreId",
    )
    val subgenres: List<SubgenreEntity>
)