package com.example.concertfinder.common

import com.example.concertfinder.BuildConfig
import com.example.concertfinder.domain.model.DistanceUnit
import com.example.concertfinder.domain.model.Radius
import java.util.Calendar

object Constants {

    // API BASE URL
    const val BASE_URL = "https://app.ticketmaster.com/discovery/v2/"

    // API SEARCH PARAMETERS
    const val PARAM_KEYWORD = "searchQuery"

    // API KEY
    const val API_KEY = BuildConfig.API_KEY

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
        Radius("", DistanceUnit.Miles)
    )

    val sortOptions = listOf(
        "Name",
        "Date",
        "Distance",
        "Relevance"
    )

    // Default values
    const val DEFAULT_RADIUS = ""
    val DEFAULT_START_DATE_TIME = getFormattedDate(Calendar.getInstance())
    const val DEFAULT_SORT_OPTION = "relevance"
    const val DEFAULT_SORT_TYPE = "desc"
    val DEFAULT_GENRE = emptySet<String>()
    val DEFAULT_SUBGENRE = emptySet<String>()
    val DEFAULT_SEGMENT = null
    const val EVENTS_NEAR_YOU_RADIUS = "25"

    // Page sizes
    const val DISCOVER_PAGE_SIZE = "5"
    const val EVENT_LIST_PAGE_SIZE = "20"
}