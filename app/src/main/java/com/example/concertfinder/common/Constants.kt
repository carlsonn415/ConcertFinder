package com.example.concertfinder.common

import android.annotation.SuppressLint
import com.example.concertfinder.domain.model.DistanceUnit
import com.example.concertfinder.domain.model.Radius
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar

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
}

// get formatted date from calendar
@SuppressLint("NewApi")
private fun getFormattedDate(calendar: Calendar): String {
    // convert calendar to instant
    val instant: Instant = calendar.toInstant()

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        .withZone(ZoneId.of("UTC"))

    return formatter.format(instant)
}