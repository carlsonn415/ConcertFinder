package com.example.lineup_app.common

import android.annotation.SuppressLint
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar

// get formatted date from calendar
@SuppressLint("NewApi")
fun getFormattedDate(calendar: Calendar): String {
    // convert calendar to instant
    val instant: Instant = calendar.toInstant()

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        .withZone(ZoneId.of("UTC"))

    return formatter.format(instant)
}