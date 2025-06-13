package com.example.concertfinder.presentation.app.components

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.concertfinder.presentation.app.AppViewModel
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
    //val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

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

    Log.d("ConcertFinderApp", "current destination: ${backStackEntry?.destination?.route}")
    Scaffold(
        // allows top bar to scroll
        //modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            if (!uiState.value.showBottomBar) {
                ConcertFinderTopBar(
                    onBackPressed = {
                        val currentDestinationRoute = navController.currentDestination?.route
                        val previousDestinationRoute = navController.previousBackStackEntry?.destination?.route

                        // check if coming from filter to event list
                        if (currentDestinationRoute == AppDestinations.FILTER
                            && previousDestinationRoute?.startsWith(AppDestinations.EVENT_LIST) == true
                            && uiState.value.areFiltersApplied
                        ) {
                            val navBackStackEntry = navController.previousBackStackEntry
                            navBackStackEntry?.savedStateHandle["filters_updated"] = true
                            viewModel.updateAreFiltersApplied(false)
                            Log.d("TopAppBarBack", "Coming from Filter to EventList, setting filters_updated flag.")
                        }
                        // navigate up
                        navController.navigateUp()
                    },
                    showFilterButton = backStackEntry?.destination?.route?.startsWith(AppDestinations.EVENT_LIST) == true,
                    showBackButton = !uiState.value.showBottomBar,
                    //scrollBehavior = scrollBehavior,
                    scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
                    onFilterSortClicked = {
                        navController.navigate(AppDestinations.FILTER)
                    },
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

        AppNavHost(
            navController = navController,
            viewModel = viewModel,
            uiState = uiState,
            modifier = modifier,
            innerPadding = innerPadding
        )
    }
}
