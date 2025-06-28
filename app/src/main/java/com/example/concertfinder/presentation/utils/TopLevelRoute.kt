package com.example.concertfinder.presentation.utils

import androidx.annotation.StringRes
import com.example.concertfinder.presentation.ui.theme.MyIcons
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
        title = R.string.discover,
        icon = MyIcons.home,
        iconOutlined = MyIcons.homeOutlined,
        route = AppDestinations.DISCOVER
    ),
    TopLevelRoute(
        title = R.string.search,
        icon = MyIcons.search,
        iconOutlined = MyIcons.searchOutlined,
        route = AppDestinations.SEARCH
    ),
    TopLevelRoute(
        title = R.string.saved_events,
        icon = MyIcons.heart,
        iconOutlined = MyIcons.heartOutlined,
        route = AppDestinations.MY_EVENTS
    ),
)
