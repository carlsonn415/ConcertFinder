package com.example.concertfinder.presentation.app.components

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.concertfinder.R
import com.example.concertfinder.common.Constants.PARAM_KEYWORD
import com.example.concertfinder.presentation.app.AppUiState
import com.example.concertfinder.presentation.app.AppViewModel
import com.example.concertfinder.presentation.common_ui.location_menu.LocationViewModel
import com.example.concertfinder.presentation.discover_screen.components.DiscoverScreen
import com.example.concertfinder.presentation.event_detail_screen.components.EventDetailScreen
import com.example.concertfinder.presentation.event_list_screen.components.EventListScreen
import com.example.concertfinder.presentation.filter_screen.components.FilterScreen
import com.example.concertfinder.presentation.saved_events_screen.components.SavedEventsScreen
import com.example.concertfinder.presentation.search_screen.components.SearchScreen
import com.example.concertfinder.presentation.utils.AppDestinations
import com.example.concertfinder.presentation.utils.topLevelRoutes

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun AppNavHost(
    navController: NavHostController,
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    viewModel: AppViewModel,
    context: Context,
    uiState: State<AppUiState>,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(0.dp),
) {
    val locationViewModel: LocationViewModel = hiltViewModel()

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
                onEventClicked = {
                    viewModel.updateCurrentEvent(it)
                    navController.navigate(AppDestinations.EVENT_DETAILS)
                },
                onClickSave = { event ->
                    viewModel.toggleEventSaved(event = event)
                    // show snackbar if event is saved or unsaved
                    if (event.saved == false) {
                        viewModel.showSnackbar(message = event.name + " " + context.getString(R.string.saved))
                    } else {
                        viewModel.showSnackbar(
                            message = event.name + " " + context.getString(R.string.unsaved),
                            onAction = {
                                viewModel.toggleEventSaved(event = event, undo = true)
                            },
                            actionLabel = context.getString(R.string.undo)
                        )
                    }
                },
                onSavedEventsLoaded = {
                    viewModel.updateSavedEventsUpdated(false)
                },
                updateSavedEvents = uiState.value.savedEventsUpdated,
                modifier = modifier,
                innerPadding = innerPadding
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
                    viewModel.updateTopBarTitle(R.string.results)
                },
                onFilterSortClicked = {
                    navController.navigate(AppDestinations.FILTER)
                },
                innerPadding = innerPadding,
                modifier = modifier,
                locationViewModel = locationViewModel
            )
        }

        // calendar screen composable
        composable(route = AppDestinations.DISCOVER) {
            DiscoverScreen(
                modifier = modifier,
                innerPadding = innerPadding,
                onSeeMoreClick = { startDateTime, endDateTime, radius, sort, segmentName, title ->
                    viewModel.onNavigateToEventList(
                        navController = navController,
                        searchQuery = "",
                        startDateTime = startDateTime,
                        endDateTime = endDateTime,
                        sort = sort,
                        radius = radius,
                        segmentName = segmentName
                    )
                    viewModel.updateTopBarTitle(title)
                },
                onEventClick = {
                    viewModel.updateCurrentEvent(it)
                    navController.navigate(AppDestinations.EVENT_DETAILS)
                },
                updateEventsSaved = uiState.value.savedEventsUpdated,
                eventSavedIds = uiState.value.savedEventsIds,
                onEventSaveClick = { event ->
                    viewModel.toggleEventSaved(event = event)
                    // show snackbar if event is saved or unsaved
                    if (event.saved == false) {
                        viewModel.showSnackbar(message = event.name + " " + context.getString(R.string.saved))
                    } else {
                        viewModel.showSnackbar(message = event.name + " " + context.getString(R.string.unsaved))
                    }
                }
            )
        }

        // event list screen composable
        composable(
            route = AppDestinations.EVENT_LIST + "/{$PARAM_KEYWORD}" + "?startDateTime={startDateTime}&endDateTime={endDateTime}&sort={sort}&radius={radius}&segmentName={segmentName}",
            arguments = listOf(
                navArgument(PARAM_KEYWORD) {
                    type = NavType.StringType
                },
                navArgument("startDateTime") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("endDateTime") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("sort") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("radius") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("segmentName") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val filtersUpdated = backStackEntry.savedStateHandle.getStateFlow("filters_updated", false)
            EventListScreen(
                onEventClicked = {
                    viewModel.updateCurrentEvent(it)
                    navController.navigate(AppDestinations.EVENT_DETAILS)
                    viewModel.updateTopBarTitle(R.string.details)
                },
                onClickSave = { event ->
                    viewModel.toggleEventSaved(event = event)
                    // show snackbar if event is saved or unsaved
                    if (event.saved == false) {
                        viewModel.showSnackbar(message = event.name + " " + context.getString(R.string.saved))
                    } else {
                        viewModel.showSnackbar(message = event.name + " " + context.getString(R.string.unsaved))
                    }
                },
                updateEventsSaved = uiState.value.savedEventsUpdated,
                eventSavedIds = uiState.value.savedEventsIds,
                filtersUpdated = filtersUpdated.collectAsState().value,
                navBackStackEntry = backStackEntry,
                scrollBehavior = topAppBarScrollBehavior,
                innerPadding = innerPadding,
                startDateTime = backStackEntry.arguments?.getString("startDateTime"),
                endDateTime = backStackEntry.arguments?.getString("endDateTime"),
                radius = backStackEntry.arguments?.getString("radius"),
                sort = backStackEntry.arguments?.getString("sort"),
                segmentName = backStackEntry.arguments?.getString("segmentName")
            )
        }

        // event details screen composable
        composable(route = AppDestinations.EVENT_DETAILS) {
            EventDetailScreen(
                event = uiState.value.currentEvent,
                navBackStackEntry = it,
                scrollBehavior = topAppBarScrollBehavior,
                modifier = modifier,
                innerPadding = innerPadding
            )
        }

        // filter screen composable
        composable(route = AppDestinations.FILTER) {
            FilterScreen(
                navController = navController,
                onFilterApplied = {
                    viewModel.updateAreFiltersApplied(true)
                },
                scrollBehavior = topAppBarScrollBehavior,
                modifier = modifier,
                innerPadding = innerPadding,
                locationViewModel = locationViewModel
            )
        }
    }
}