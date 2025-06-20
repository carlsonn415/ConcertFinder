package com.example.concertfinder.presentation.app.components

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.concertfinder.R
import com.example.concertfinder.common.Constants.PARAM_KEYWORD
import com.example.concertfinder.presentation.app.AppUiState
import com.example.concertfinder.presentation.app.AppViewModel
import com.example.concertfinder.presentation.calendar_screen.components.CalendarScreen
import com.example.concertfinder.presentation.event_detail_screen.components.EventDetailScreen
import com.example.concertfinder.presentation.event_list_screen.components.EventListScreen
import com.example.concertfinder.presentation.filter_screen.components.FilterScreen
import com.example.concertfinder.presentation.saved_events_screen.components.SavedEventsScreen
import com.example.concertfinder.presentation.search_screen.components.SearchScreen
import com.example.concertfinder.presentation.utils.AppDestinations
import com.example.concertfinder.presentation.utils.topLevelRoutes

@RequiresApi(Build.VERSION_CODES.O)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun AppNavHost(
    navController: NavHostController,
    viewModel: AppViewModel,
    context: Context,
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
                onEventClicked = {
                    viewModel.updateCurrentEvent(it)
                    navController.navigate(AppDestinations.EVENT_DETAILS)
                },
                onClickSave = { event ->
                    viewModel.toggleEventSaved(event = event)
                    viewModel.updateSavedEventsUpdated(true)
                    // show toast if event is saved or unsaved
                    // TODO: make these snackbars
                    if (event.saved == false) {
                        Toast.makeText(context, event.name + " " + context.getString(R.string.saved), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, event.name + " " + context.getString(R.string.unsaved), Toast.LENGTH_SHORT).show()
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
        ) { backStackEntry ->
            val filtersUpdated = backStackEntry.savedStateHandle.getStateFlow("filters_updated", false)

            EventListScreen(
                onEventClicked = {
                    viewModel.updateCurrentEvent(it)
                    navController.navigate(AppDestinations.EVENT_DETAILS)
                },
                onClickSave = { event ->
                    viewModel.toggleEventSaved(event = event)
                    viewModel.updateSavedEventsUpdated(true)
                    // show toast if event is saved or unsaved
                    // TODO: make these snackbars
                    if (event.saved == false) {
                        Toast.makeText(context, event.name + " " + context.getString(R.string.saved), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, event.name + " " + context.getString(R.string.unsaved), Toast.LENGTH_SHORT).show()
                    }
                },
                filtersUpdated = filtersUpdated.collectAsState().value,
                navBackStackEntry = backStackEntry,
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
                navController = navController,
                onFilterApplied = {
                    viewModel.updateAreFiltersApplied(true)
                },
                modifier = modifier,
                innerPadding = innerPadding
            )
        }
    }
}