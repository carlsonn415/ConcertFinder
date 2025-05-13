package com.example.concertfinder.ui.utils

import androidx.annotation.StringRes
import com.example.concertfinder.R

enum class ConcertFinderScreen(
    @StringRes val title: Int
) {
    MyEvents(title = R.string.events),
    Calendar(title = R.string.calendar),
    Search(title = R.string.search),
    EventDetails(title = R.string.event_details),
    DayDetails(title = R.string.day_details),
    SearchResults(title = R.string.results),
}