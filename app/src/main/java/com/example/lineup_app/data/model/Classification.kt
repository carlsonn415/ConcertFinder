package com.example.lineup_app.data.model

data class Classification(
    val segment: Segment,
    val genres: List<Genre>,
)

data class Segment(
    val id: String,
    val name: String
)

data class Genre(
    val id: String,
    val name: String,
    val subgenres: List<Subgenre>
)

data class Subgenre(
    val id: String,
    val name: String
)

