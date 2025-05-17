package com.example.concertfinder.model

data class Radius(
    val radius: String,
    val unit: String
)

val radiusOptions = listOf(
    Radius("10", "Miles"),
    Radius("25", "Miles"),
    Radius("50", "Miles"),
    Radius("75", "Miles"),
    Radius("100", "Miles"),
    Radius("250", "Miles"),
    Radius("500", "Miles"),
    Radius("1000", "Miles"),
)
