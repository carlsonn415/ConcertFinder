package com.example.lineup_app.common

import com.example.lineup_app.BuildConfig
import java.util.Calendar

object Constants {

    // API BASE URL
    const val BASE_URL = "https://app.ticketmaster.com/discovery/v2/"

    // API SEARCH PARAMETERS
    const val PARAM_KEYWORD = "searchQuery"

    // API KEY
    const val TICKETMASTER_API_KEY = BuildConfig.TICKETMASTER_API_KEY

    val sortOptions = listOf(
        "Name",
        "Date",
        "Distance",
        "Relevance"
    )

    val radiusValues = listOf(
        "10", "20", "30", "40", "50", "60", "70", "80", "90", "100",
        "150", "200", "250", "300",
        "500",
        "1000",
        ""
    )

    val radiusDisplayValues = listOf(
        "10", "20", "30", "40", "50", "60", "70", "80", "90", "100",
        "150", "200", "250", "300",
        "500",
        "1000",
        "Unlimited"
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
    const val DEFAULT_LATITUDE = 40.7128
    const val DEFAULT_LONGITUDE = -74.0060
    const val DEFAULT_ADDRESS = "New York, NY"

    // Page sizes
    const val DISCOVER_PAGE_SIZE = "5"
    const val EVENT_LIST_PAGE_SIZE = "20"
}