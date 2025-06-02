package com.example.concertfinder.presentation.app.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.concertfinder.common.Constants.PARAM_KEYWORD
import com.example.concertfinder.presentation.app.AppUiState
import com.example.concertfinder.presentation.app.AppViewModel
import com.example.concertfinder.presentation.calendar_screen.components.CalendarScreen
import com.example.concertfinder.presentation.event_detail_screen.components.EventDetailScreen
import com.example.concertfinder.presentation.event_list_screen.components.EventListScreen
import com.example.concertfinder.presentation.filter_screen.components.FilterScreen
import com.example.concertfinder.presentation.saved_events_screen.components.SavedEventsScreen
import com.example.concertfinder.presentation.search_screen.components.SearchScreen
import com.example.concertfinder.presentation.utils.AppContentType
import com.example.concertfinder.presentation.utils.AppDestinations
import com.example.concertfinder.presentation.utils.topLevelRoutes


@OptIn(ExperimentalMaterial3Api::class)
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

    // create scroll behavior for top app bar
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

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

    // update show fab based on current destination
    LaunchedEffect(navController.currentDestination?.route == AppDestinations.EVENT_DETAILS) {
        viewModel.updateFabVisibility(navController.currentDestination?.route == AppDestinations.EVENT_DETAILS)
    }

    Scaffold(
        // allows top bar to scroll
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            if (!uiState.value.showBottomBar) {
                ConcertFinderTopBar(
                    onBackPressed = {
                        // navigate back
                        navController.navigateUp()
                    },
                    showBackButton = !uiState.value.showBottomBar,
                    scrollBehavior = scrollBehavior,
                    modifier = modifier
                )
            }
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
        },
        floatingActionButton = {
            // animates fab in and out
            AnimatedVisibility(
                visible = uiState.value.showFab,
                enter = fadeIn(tween(durationMillis = 100)),
                exit = fadeOut(tween(durationMillis = 0))
            ) {
                FloatingAppButton(
                    onClick = {
                        viewModel.toggleCurrentEventSaved()
                    },
                    filled = uiState.value.currentEvent.saved
                )
            }
        }
    ) { innerPadding ->

        ConcertFinderNavHost(
            navController = navController,
            viewModel = viewModel,
            uiState = uiState,
            modifier = modifier,
            innerPadding = innerPadding
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
private fun ConcertFinderNavHost(
    navController: NavHostController,
    viewModel: AppViewModel,
    uiState: State<AppUiState>,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(0.dp),
) {
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
        // saved events screen composable
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
                onOpenFilterPreferences = {
                    navController.navigate(AppDestinations.FILTER)
                },
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
                    viewModel.onNavigateToEventList(
                        navController = navController,
                        searchQuery = ""
                    )
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
                onEventClicked = {
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

        // filter screen composable
        composable(route = AppDestinations.FILTER) {
            FilterScreen(
                modifier = modifier,
                innerPadding = innerPadding
            )
        }
    }
}
