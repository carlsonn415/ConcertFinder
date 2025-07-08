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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.concertfinder.R
import com.example.concertfinder.data.local.AppSnackbarManager
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

    // get context
    val context = LocalContext.current

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

    // initialize snackbar manager
    AppSnackbarManager.init(viewModel)

    // update show fab and top bar title based on current destination
    LaunchedEffect(backStackEntry) {
        viewModel.updateFabVisibility(backStackEntry?.destination?.route == AppDestinations.EVENT_DETAILS)

//        if (backStackEntry?.destination?.route == AppDestinations.EVENT_DETAILS) {
//            previousTopBarTitle = uiState.value.topBarTitle
//            viewModel.updateTopBarTitle(R.string.details)
//            viewModel.updateCurrentEvent(uiState.value.currentEvent)
//        } else if (backStackEntry?.destination?.route == AppDestinations.FILTER) {
//            viewModel.updateTopBarTitle(R.string.filter_with_gear)
//        } else if (backStackEntry?.destination?.route?.startsWith(AppDestinations.EVENT_LIST) == true
//            && navController.previousBackStackEntry?.destination?.route == AppDestinations.SEARCH
//        ) {
//            viewModel.updateTopBarTitle(R.string.results)
//        }
    }

    val snackbarHostState = remember { SnackbarHostState() }

    // Collect snackbar messages from the ViewModel
    LaunchedEffect(Unit) {
        viewModel.snackbarMessages.collect { snackbarMessage ->
            // Dismiss previous snackbar if any, to avoid queueing
            snackbarHostState.currentSnackbarData?.dismiss()

            val result = snackbarHostState.showSnackbar(
                message = snackbarMessage.message,
                actionLabel = snackbarMessage.actionLabel,
                duration = snackbarMessage.duration,
                withDismissAction = snackbarMessage.actionLabel == null
            )
            when (result) {
                SnackbarResult.ActionPerformed -> {
                    snackbarMessage.onAction?.invoke()
                }
                SnackbarResult.Dismissed -> {
                    snackbarHostState.currentSnackbarData?.dismiss()
                }
            }
        }
    }


    Log.d("ConcertFinderApp", "current destination: ${backStackEntry?.destination?.route}")
    Scaffold(
        // allows top bar to scroll only when it is visible
        modifier = if (!uiState.value.showBottomBar) { modifier.nestedScroll(scrollBehavior.nestedScrollConnection) } else modifier,

        topBar = {
            val currentDestinationRoute = navController.currentDestination?.route
            val previousDestinationRoute = navController.previousBackStackEntry?.destination?.route

            val showFilterButton = currentDestinationRoute?.startsWith(AppDestinations.EVENT_LIST) == true
                && previousDestinationRoute != AppDestinations.DISCOVER

            if (!uiState.value.showBottomBar) {
                ConcertFinderTopBar(
                    onBackPressed = {
                        // check if coming from filter to event list
                        if (currentDestinationRoute == AppDestinations.FILTER
                            && previousDestinationRoute?.startsWith(AppDestinations.EVENT_LIST) == true
                            && uiState.value.areFiltersAppliedFlag
                        ) {
                            navController.previousBackStackEntry?.savedStateHandle["filters_updated"] = true
                            viewModel.updateAreFiltersApplied(false)
                            Log.d("TopAppBarBack", "Coming from Filter to EventList, setting filters_updated flag.")
                        }
                        if (uiState.value.topBarTitleStack.size > 1) {
                            viewModel.popTopBarTitle()
                        }
                        if (currentDestinationRoute == AppDestinations.EVENT_DETAILS && previousDestinationRoute?.startsWith(AppDestinations.EVENT_LIST) == true) {
                            viewModel.updateEventListScreenLoadNewEventsFlag(false)
                            navController.navigateUp()
                            viewModel.updateEventListScreenLoadNewEventsFlag(true)
                        } else {
                            navController.navigateUp()
                        }
                    },
                    // only show filter button if on event list screen and not coming from discover screen
                    showFilterButton = showFilterButton,
                    showBackButton = !uiState.value.showBottomBar,
                    scrollBehavior = scrollBehavior,
                    onFilterSortClicked = {
                        navController.navigate(AppDestinations.FILTER)
                        viewModel.pushOntoTopBarTitle(R.string.filter_with_gear)
                    },
                    // set title based on current destination
                    titleId = uiState.value.topBarTitleStack.last(),
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
                    },
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
                val filled = uiState.value.currentEvent.saved
                FloatingAppButton(
                    onClick = {
                        viewModel.toggleEventSaved(event = uiState.value.currentEvent)
                        viewModel.updateAllEventSavedFlags(true)
                        // show snackbar if event is saved or unsaved
                        if (filled == false) {
                            viewModel.showSnackbar(message = uiState.value.currentEvent.name + " " + context.getString(R.string.saved))
                        } else {
                            viewModel.showSnackbar(
                                message = uiState.value.currentEvent.name + " " + context.getString(R.string.unsaved),
                                onAction = {
                                    viewModel.toggleEventSaved(event = uiState.value.currentEvent, undo = true)
                                },
                                actionLabel = context.getString(R.string.undo)
                            )
                        }
                    },
                    filled = filled
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->

        AppNavHost(
            navController = navController,
            viewModel = viewModel,
            topAppBarScrollBehavior = scrollBehavior,
            context = context,
            uiState = uiState,
            modifier = modifier,
            innerPadding = innerPadding
        )
    }
}
