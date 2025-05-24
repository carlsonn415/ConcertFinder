package com.example.concertfinder.presentation.app.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.concertfinder.R
import com.example.concertfinder.common.Constants.PARAM_KEYWORD
import com.example.concertfinder.presentation.app.AppViewModel
import com.example.concertfinder.presentation.calendar_screen.components.CalendarScreen
import com.example.concertfinder.presentation.event_detail_screen.components.EventDetailScreen
import com.example.concertfinder.presentation.event_list_screen.components.EventListScreen
import com.example.concertfinder.presentation.saved_events_screen.components.SavedEventsScreen
import com.example.concertfinder.presentation.search_screen.components.SearchScreen
import com.example.concertfinder.presentation.utils.AppContentType
import com.example.concertfinder.presentation.utils.AppDestinations
import com.example.concertfinder.presentation.utils.TopLevelRoute
import com.example.concertfinder.presentation.utils.topLevelRoutes


@RequiresApi(Build.VERSION_CODES.O)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun ConcertFinderApp(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController(),
    windowSize: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
) {
    // get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()

    // collect ui state from view model
    val uiState = viewModel.uiState.collectAsState()

    /* TODO: add support for different screen sizes */
    val contentType = when(windowSize) {
        WindowWidthSizeClass.Compact -> AppContentType.Compact
        WindowWidthSizeClass.Medium -> AppContentType.Medium
        WindowWidthSizeClass.Expanded -> AppContentType.Expanded
        else -> AppContentType.Compact
    }

    // update show bottom bar based on current destination
    if (backStackEntry?.destination?.route in topLevelRoutes.map { it.route }) {
        viewModel.updateShowBottomBar(true)
    } else {
        viewModel.updateShowBottomBar(false)
    }

    Scaffold(
        // add top bar and bottom bar to scaffold
        topBar = {
            ConcertFinderTopBar(
                onBackPressed = {
                    // navigate back
                    navController.navigateUp()
                },
                showBackButton = !uiState.value.showBottomBar
            )
        },
        bottomBar = {
            if (uiState.value.showBottomBar) {
                ConcertFinderNavigationBar(
                    backStackEntry = backStackEntry,
                    onClick = {

                        navController.navigate(it.route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when re-selecting the same item
                            launchSingleTop = true
                            // Restore state when re-selecting a previously selected item
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = topLevelRoutes[0].route,
            modifier = modifier,
            enterTransition = {
                fadeIn()
            },
            exitTransition = {
                fadeOut()
            }
        ) {
            // my events screen composable
            composable(route = AppDestinations.MY_EVENTS) {
                SavedEventsScreen(
                    onClick = { event ->
                        navController.navigate(AppDestinations.EVENT_DETAILS)
                    },
                    modifier = modifier
                )
            }

            // search screen composable
            composable(route = AppDestinations.SEARCH) {
                SearchScreen(
                    onSearch = {
                        viewModel.onNavigateToEventList(
                            navController = navController,
                            searchQuery = it
                        )
                    },
                    innerPadding = innerPadding,
                    modifier = modifier
                )
            }

            // calendar screen composable
            composable(route = AppDestinations.CALENDAR) {
                CalendarScreen(
                    onClick = {
                        // TODO: navigate to event list screen
                    },
                    modifier = modifier
                )
            }

            // event list screen composable
            composable(
                route = AppDestinations.EVENT_LIST + "/{$PARAM_KEYWORD}",
                arguments = listOf(
                    navArgument(PARAM_KEYWORD) {
                        type = NavType.StringType
                    }
                )
            ) {
                EventListScreen(
                    onClick = {
                        viewModel.updateCurrentEvent(it)
                        navController.navigate(AppDestinations.EVENT_DETAILS)
                    },
                    modifier = modifier,
                    innerPadding = innerPadding
                )
            }

            // event details screen composable
            composable(route = AppDestinations.EVENT_DETAILS) {
                EventDetailScreen(
                    event = uiState.value.currentEvent,
                    modifier = modifier,
                    innerPadding = innerPadding
                )
            }
        }
    }
}



@Composable
fun ConcertFinderNavigationBar(
    backStackEntry: NavBackStackEntry?,
    onClick: (TopLevelRoute<String>) -> Unit,
) {
    NavigationBar {
        val currentDestination = backStackEntry?.destination

        topLevelRoutes.forEach { topLevelRoute ->
            NavigationBarItem(
                icon = {
                    if (currentDestination?.hierarchy?.any {it.route == topLevelRoute.route} == true) {
                        Icon(
                            topLevelRoute.icon,
                            contentDescription = stringResource(topLevelRoute.title)
                        )
                    } else {
                        Icon(
                            topLevelRoute.iconOutlined,
                            contentDescription = stringResource(topLevelRoute.title)
                        )
                    }
                },
                label = {
                    Text(
                        text = stringResource(topLevelRoute.title)
                    )
                },
                selected = currentDestination?.hierarchy?.any {it.route == topLevelRoute.route} == true,
                onClick = { onClick(topLevelRoute) },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConcertFinderTopBar(
    onBackPressed: () -> Unit,
    showBackButton: Boolean,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_icon),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = modifier
                        .size(dimensionResource(id = R.dimen.app_icon_size))
                        .padding(end = 8.dp)
                )
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(
                    onClick = { onBackPressed() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_button)
                    )
                }
            }
        }
    )
}
