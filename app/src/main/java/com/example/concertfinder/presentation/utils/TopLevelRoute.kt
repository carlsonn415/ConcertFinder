package com.example.concertfinder.presentation.utils

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.concertfinder.R


data class TopLevelRoute<T : Any>(
    @StringRes val title: Int,
    val icon: ImageVector,
    val iconOutlined: ImageVector,
    val route: T,
)

val topLevelRoutes = listOf(
    TopLevelRoute(
        title = R.string.events,
        icon = Icons.Default.Home,
        iconOutlined = Icons.Outlined.Home,
        route = AppDestinations.MY_EVENTS
    ),

    TopLevelRoute(
        title = R.string.search,
        icon = Icons.Default.Search,
        iconOutlined = Icons.Outlined.Search,
        route = AppDestinations.SEARCH
    ),
    TopLevelRoute(
        title = R.string.calendar,
        icon = Icons.Default.DateRange,
        iconOutlined = Icons.Outlined.DateRange,
        route = AppDestinations.CALENDAR
    ),
)
