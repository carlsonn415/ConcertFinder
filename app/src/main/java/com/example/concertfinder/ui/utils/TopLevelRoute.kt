package com.example.concertfinder.ui.utils

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.concertfinder.R


data class TopLevelRoute<T : Any>(
    @StringRes val title: Int,
    val icon: ImageVector,
    val route: T,
)

val topLevelRoutes = listOf(
    TopLevelRoute(
        title = R.string.events,
        icon = Icons.Default.Home,
        route = ConcertFinderScreen.MyEvents.name
    ),

    TopLevelRoute(
        title = R.string.search,
        icon = Icons.Default.Search,
        route = ConcertFinderScreen.Search.name
    ),
    TopLevelRoute(
        title = R.string.calendar,
        icon = Icons.Default.DateRange,
        route = ConcertFinderScreen.Calendar.name
    ),
)
