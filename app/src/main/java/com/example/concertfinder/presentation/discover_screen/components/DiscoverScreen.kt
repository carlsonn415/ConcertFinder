package com.example.concertfinder.presentation.discover_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.concertfinder.R
import com.example.concertfinder.data.model.Event
import com.example.concertfinder.presentation.discover_screen.DiscoverScreenViewModel

@Composable
fun DiscoverScreen(
    onSeeMoreClick: () -> Unit,
    onEventClick: (event: Event) -> Unit,
    onEventSaveClick: (event: Event) -> Unit,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: DiscoverScreenViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small)),
        contentPadding = innerPadding
    ) {
        // events this weekend
        item() {
            DiscoverScreenListItem(
                events = uiState.value.eventsThisWeekend,
                onSeeMoreClick = onSeeMoreClick,
                onEventClick = onEventClick,
                onEventSaveClick = onEventSaveClick,
                title = "Happening This Weekend"
            )
        }

        // events near you
        item() {
            DiscoverScreenListItem(
                events = uiState.value.eventsNearYou,
                onSeeMoreClick = onSeeMoreClick,
                onEventClick = onEventClick,
                onEventSaveClick = onEventSaveClick,
                title = "Near You"
            )
        }

        // music events
        item() {
            DiscoverScreenListItem(
                events = uiState.value.musicEvents,
                onSeeMoreClick = onSeeMoreClick,
                onEventClick = onEventClick,
                onEventSaveClick = onEventSaveClick,
                title = "Music",
            )
        }

        // sports events
        item() {
            DiscoverScreenListItem(
                events = uiState.value.sportsEvents,
                onSeeMoreClick = onSeeMoreClick,
                onEventClick = onEventClick,
                onEventSaveClick = onEventSaveClick,
                title = "Sports"
            )
        }

        // arts & theater events
        item() {
            DiscoverScreenListItem(
                events = uiState.value.artsEvents,
                onSeeMoreClick = onSeeMoreClick,
                onEventClick = onEventClick,
                onEventSaveClick = onEventSaveClick,
                title = "Arts & Theater"
            )
        }
    }
}