package com.example.concertfinder.common

import com.example.concertfinder.domain.model.DistanceUnit
import com.example.concertfinder.domain.model.Radius

object Constants {

    // API BASE URL
    const val BASE_URL = "https://app.ticketmaster.com/discovery/v2/"

    // API SEARCH PARAMETERS
    const val PARAM_KEYWORD = "searchQuery"

    // IMPORTANT: STORE API KEY ON SERVER AND NOT IN CODE BEFORE DISTRIBUTION
    const val PARAM_API_KEY = "0FhVbrCAPJdEh7PBbHl8Gk8T6OLpKJI2"

    // List of radius options user can select
    val radiusOptions = listOf(
        Radius("10", DistanceUnit.Miles),
        Radius("25", DistanceUnit.Miles),
        Radius("50", DistanceUnit.Miles),
        Radius("75", DistanceUnit.Miles),
        Radius("100", DistanceUnit.Miles),
        Radius("250", DistanceUnit.Miles),
        Radius("500", DistanceUnit.Miles),
        Radius("1000", DistanceUnit.Miles),
    )

    val sortOptions = listOf(
        "name",
        "date",
        "distance",
        "relevance"
    )

    val sortTypes = listOf(
        "asc",
        "desc"
    )
}