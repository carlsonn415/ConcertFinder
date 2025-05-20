package com.example.concertfinder.common

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
        Radius("10", "Miles"),
        Radius("25", "Miles"),
        Radius("50", "Miles"),
        Radius("75", "Miles"),
        Radius("100", "Miles"),
        Radius("250", "Miles"),
        Radius("500", "Miles"),
        Radius("1000", "Miles"),
    )
}