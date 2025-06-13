package com.example.concertfinder.presentation.saved_events_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
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
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: SavedEventsViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxWidth()
    ) {
        if (uiState.value.events is Resource.Success) {
            val events = uiState.value.events.data ?: emptyList()
            LazyColumn(
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