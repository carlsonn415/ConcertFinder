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
import com.example.concertfinder.R
import com.example.concertfinder.common.Resource
import com.example.concertfinder.data.model.Event
import com.example.concertfinder.domain.model.DistanceUnit
import com.example.concertfinder.presentation.common_ui.ErrorScreen
import com.example.concertfinder.presentation.common_ui.EventListItem
import com.example.concertfinder.presentation.common_ui.LoadingScreen
import com.example.concertfinder.presentation.event_list_screen.EventListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun EventListScreen(
    onEventClicked: (Event) -> Unit,
    filtersUpdated: Boolean,
    navBackStackEntry: NavBackStackEntry,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: EventListViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState.collectAsState()

    val lazyListState = rememberLazyListState()

    // Pagination loads more when user gets near the end
    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = lazyListState.layoutInfo
            val totalItemsCount = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1

            // Trigger load if there are 5 items left to scroll
            totalItemsCount > 0 && lastVisibleItemIndex >= (totalItemsCount - 5) && !uiState.value.isLoadingMore && uiState.value.canLoadMore
        }
    }

    // get events from view model when screen is loaded
    LaunchedEffect(shouldLoadMore) {
        if (filtersUpdated) {
            Log.d("EventListScreen", "Filters updated")
            viewModel.getEvents()
            navBackStackEntry.savedStateHandle["filters_updated"] = false
        } else if (shouldLoadMore) {
            Log.d("EventListScreen", "Loading more events")
            viewModel.loadMoreEvents()
        }
    }

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxWidth()
    ) {
        if (uiState.value.eventsResource is Resource.Success) {
            PaginatedEventList(
                events = uiState.value.eventsResource.data ?: emptyList(),
                onEventClicked = onEventClicked,
                viewModel = viewModel,
                isLoadingMore = uiState.value.isLoadingMore,
                lazyListState = lazyListState,
                modifier = Modifier.fillMaxSize()
            )
        } else if (uiState.value.eventsResource is Resource.Loading) {
            LoadingScreen(modifier = Modifier.fillMaxSize())
        } else if (uiState.value.eventsResource is Resource.Error) {
            ErrorScreen(
                message = uiState.value.eventsResource.message ?: stringResource(R.string.unknown_error),
            )
        }
    }
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun PaginatedEventList(
    events: List<Event>,
    onEventClicked: (Event) -> Unit,
    viewModel: EventListViewModel,
    isLoadingMore: Boolean,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
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
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.padding_medium))
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}