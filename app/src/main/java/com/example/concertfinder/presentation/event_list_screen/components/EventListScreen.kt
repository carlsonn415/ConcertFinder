package com.example.concertfinder.presentation.event_list_screen.components

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.concertfinder.R
import com.example.concertfinder.common.Resource
import com.example.concertfinder.data.model.Event
import com.example.concertfinder.domain.model.DistanceUnit
import com.example.concertfinder.presentation.common_ui.ErrorScreen
import com.example.concertfinder.presentation.common_ui.EventListItem
import com.example.concertfinder.presentation.common_ui.LoadingScreen
import com.example.concertfinder.presentation.event_list_screen.EventListViewModel
import com.example.concertfinder.presentation.utils.AppDestinations

@OptIn(ExperimentalMaterial3Api::class)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun EventListScreen(
    onEventClicked: (Event) -> Unit,
    onClickSave: (Event) -> Unit,
    updateEventsSaved: Boolean,
    eventSavedIds: Set<String>,
    filtersUpdated: Boolean,
    navBackStackEntry: NavBackStackEntry,
    scrollBehavior: TopAppBarScrollBehavior,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: EventListViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState.collectAsState()

    val lazyListState = rememberLazyListState()

    // Pagination loads more when user gets near the end
    val shouldRequestNextPage by remember {
        derivedStateOf {
            val isLoading = uiState.value.isLoadingMore
            val canLoad = uiState.value.canLoadMore
            val layoutInfo = lazyListState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisible = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1
            val threshold = totalItems - 5

            if (isLoading || !canLoad) {
                false
            } else {
                if (totalItems == 0) {
                    false
                } else {
                    lastVisible >= threshold
                }
            }
        }
    }

    // This LaunchedEffect will only run if shouldRequestNextPage becomes true
    // AND a load is not already in progress.
    LaunchedEffect(shouldRequestNextPage) {
        Log.d("EventListScreen", "shouldRequestNextPage: $shouldRequestNextPage")
        if (shouldRequestNextPage && !uiState.value.isLoadingMore) { // Double check isLoadingMore here
            Log.d("EventListScreen", "Requesting more events")
            viewModel.loadMoreEvents()
        }
    }

    LaunchedEffect(Unit) {
        if (filtersUpdated) {
            Log.d("EventListScreen", "Filters updated, fetching initial events")
            viewModel.getEvents(hasLoadedOnce = false) // This should reset any pagination state in the ViewModel
            navBackStackEntry.savedStateHandle["filters_updated"] = false
            Log.d("EventListScreen", "Scroll to top")
            lazyListState.scrollToItem(0)
        }
    }

    LaunchedEffect(updateEventsSaved) {
        if (updateEventsSaved) {
            Log.d("EventListScreen", "Saved events ids: $eventSavedIds")
            viewModel.updateEventsSaved(eventSavedIds)
            Log.d("EventListScreen", "Updated events saved")
        }
    }

    LaunchedEffect(navBackStackEntry) {
        // Check if the current destination is THIS screen
        if (navBackStackEntry.destination.route?.startsWith(AppDestinations.EVENT_LIST) == true) {
            // Access the state within the scroll behavior
            val topAppBarState = scrollBehavior.state
            if (topAppBarState.heightOffset != 0f || topAppBarState.contentOffset != 0f) {
                Log.d("TopAppBarReset", "Resetting TopAppBar state for ${navBackStackEntry.destination.route}")
                topAppBarState.heightOffset = 0f
                topAppBarState.contentOffset = 0f
            }
        }
    }


    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxWidth()
    ) {
        when (val eventsResource = uiState.value.eventsResource) {
            is Resource.Success -> {
                PaginatedEventList(
                    events = eventsResource.data ?: emptyList(),
                    onEventClicked = onEventClicked,
                    onClickSaved = onClickSave,
                    viewModel = viewModel,
                    isLoadingMore = uiState.value.isLoadingMore, // Pass this for the progress indicator
                    lazyListState = lazyListState,
                    modifier = Modifier.fillMaxSize()
                )
            }
            is Resource.Loading -> {
                // Show loading only if there's no data yet.
                // If there's data, the list will show and a small spinner for pagination.
                if (eventsResource.data.isNullOrEmpty()) {
                    LoadingScreen(modifier = Modifier.fillMaxSize())
                } else {
                    // Data exists, show it, pagination spinner will be at the bottom if isLoadingMore is true
                    PaginatedEventList(
                        events = eventsResource.data, // Show existing data
                        onEventClicked = onEventClicked,
                        onClickSaved = onClickSave,
                        viewModel = viewModel,
                        isLoadingMore = uiState.value.isLoadingMore,
                        lazyListState = lazyListState,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            is Resource.Error -> {
                ErrorScreen(
                    message = eventsResource.message ?: stringResource(R.string.unknown_error),
                )
            }
        }
    }
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun PaginatedEventList(
    events: List<Event>,
    onEventClicked: (Event) -> Unit,
    onClickSaved: (Event) -> Unit,
    viewModel: EventListViewModel,
    isLoadingMore: Boolean,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
        modifier = modifier
    ) {
        items(events) { event ->

            // Gets distance from location
            val distanceFromLocation = viewModel.getDistanceFromLocation(
                event.location?.latitude ?: event.embedded?.venues?.get(0)?.location?.latitude,
                event.location?.longitude
                    ?: event.embedded?.venues?.get(0)?.location?.longitude,
                DistanceUnit.Miles // TODO: Allow user to select distance unit
            )

            EventListItem(
                event = event,
                onClick = onEventClicked,
                onClickSave = { clickedEvent ->
                    onClickSaved(clickedEvent)
                    viewModel.changeEventSaved(clickedEvent.id.toString(), !clickedEvent.saved)
                },
                distanceToEvent = distanceFromLocation,
                startDateTime = viewModel.getFormattedEventStartDates(event.dates) ?: "No start date found",
                imageUrl = viewModel.getImageUrl(event.images) ?: "",
                modifier = modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium))
            )
        }

        if (isLoadingMore) {
            item {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.padding_large))
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}