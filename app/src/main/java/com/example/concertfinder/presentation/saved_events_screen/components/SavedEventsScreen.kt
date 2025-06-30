package com.example.concertfinder.presentation.saved_events_screen.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.concertfinder.R
import com.example.concertfinder.common.Resource
import com.example.concertfinder.data.model.Event
import com.example.concertfinder.domain.model.DistanceUnit
import com.example.concertfinder.presentation.common_ui.ErrorScreen
import com.example.concertfinder.presentation.common_ui.EventListItem
import com.example.concertfinder.presentation.common_ui.LoadingScreen
import com.example.concertfinder.presentation.saved_events_screen.SavedEventsViewModel

@Composable
fun SavedEventsScreen(
    onEventClicked: (Event) -> Unit,
    onClickSave: (Event) -> Unit,
    onSavedEventsLoaded: () -> Unit,
    updateSavedEvents: Boolean,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: SavedEventsViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    val lazyListState = rememberLazyListState()

    LaunchedEffect(updateSavedEvents) {
        if (updateSavedEvents) {
            viewModel.getSavedEvents()
            onSavedEventsLoaded()
        }
    }

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .padding(innerPadding)
            .padding(top = dimensionResource(R.dimen.padding_medium))
            .fillMaxWidth()
    ) {
        val events = uiState.value.events
        if (events is Resource.Success && events.data != null) {
            if (events.data != emptyList<Event>()) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
                    state = lazyListState,
                ) {
                    Log.d("SavedEventsScreen", "events size: ${events.data.size}")
                    items(events.data) { event ->

                        val firstVenue = event.embedded?.venues?.firstOrNull()
                        val latitude = event.location?.latitude ?: firstVenue?.location?.latitude
                        val longitude = event.location?.longitude ?: firstVenue?.location?.longitude

                        val distanceFromLocation = if (latitude != null && longitude != null) {
                            viewModel.getDistanceFromLocation(
                                latitude,
                                longitude,
                                DistanceUnit.Miles
                            )
                        } else {
                            // Handle case where location is completely unavailable
                            "Unknown distance"
                        }

                        EventListItem(
                            event = event,
                            onClick = onEventClicked,
                            onClickSave = { event ->
                                onClickSave(event)
                                viewModel.removeEvent(event.id.toString())
                            },
                            distanceToEvent = distanceFromLocation,
                            startDateTime = viewModel.getFormattedEventStartDates(event.dates) ?: "No start date found",
                            imageUrl = viewModel.getImageUrl(event.images) ?: "",
                            modifier = modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium))
                        )
                    }
                }
            }
            else {
                ErrorScreen(
                    message = "No saved events found",
                )
            }
        } else if (uiState.value.events is Resource.Loading) {
            LoadingScreen(modifier = Modifier.fillMaxSize())
        } else if (uiState.value.events is Resource.Error) {
            ErrorScreen(
                message = uiState.value.events.message ?: stringResource(R.string.unknown_error),
            )
        }
    }
}