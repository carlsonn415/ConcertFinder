package com.example.concertfinder.presentation.discover_screen.components

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.concertfinder.R
import com.example.concertfinder.common.Constants
import com.example.concertfinder.data.model.Event
import com.example.concertfinder.domain.model.DistanceUnit
import com.example.concertfinder.presentation.discover_screen.DiscoverScreenViewModel
import com.example.concertfinder.presentation.ui.theme.MyIcons

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DiscoverScreen(
    onSeeMoreClick: (
        startDateTime: String?,
        endDateTime: String?,
        radius: String?,
        sort: String?,
        segmentName: String?,
        titleId: Int,
    ) -> Unit,
    onEventClick: (event: Event) -> Unit,
    onEventSaveClick: (event: Event) -> Unit,
    updateEventsSaved: Boolean,
    eventSavedIds: Set<String>,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: DiscoverScreenViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(updateEventsSaved) {
        if (updateEventsSaved) {
            Log.d("DiscoverScreen", "Saved events ids: $eventSavedIds")
            viewModel.updateEventsSaved(eventSavedIds)
            Log.d("DiscoverScreen", "Updated events saved")
        }
    }

    PullToRefreshBox(
        isRefreshing = uiState.value.isRefreshing,
        onRefresh = {
            viewModel.refreshAllEvents()
        },
        modifier = modifier
            .padding(innerPadding)
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small)),
            modifier = modifier
                .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
                .verticalScroll(rememberScrollState())
        ) {

            // events this weekend
            DiscoverScreenListItem(
                events = uiState.value.eventsThisWeekend,
                onSeeMoreClick = {
                    onSeeMoreClick(
                        viewModel.startDateTime,
                        viewModel.endDateTime,
                        null,
                        null,
                        null,
                        R.string.this_weekend
                    )
                },
                onEventClick = onEventClick,
                onEventSaveClick = {
                    onEventSaveClick(it)
                    viewModel.changeEventSaved(
                        id = it.id.toString(),
                        save = !it.saved,
                        eventList = uiState.value.eventsThisWeekend.data ?: emptyList(),
                        category = 0
                    )
                },
                getImageUrl = { images ->
                    viewModel.getImageUrl(
                        images = images,
                        aspectRatio = "4_3",
                        minImageWidth = 400
                    )
                },
                getDistanceToEvent = { event ->
                    // Gets distance from location
                    val distanceFromLocation = viewModel.getDistanceFromLocation(
                        event.location?.latitude
                            ?: event.embedded?.venues?.get(0)?.location?.latitude,
                        event.location?.longitude
                            ?: event.embedded?.venues?.get(0)?.location?.longitude,
                        DistanceUnit.Miles
                    )

                    distanceFromLocation
                },
                getStartDateTime = { dateData ->
                    viewModel.getFormattedEventStartDates(dateData)
                },
                title = stringResource(R.string.this_weekend)
            )

            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium_small)))

            // events near you
            DiscoverScreenListItem(
                events = uiState.value.eventsNearYou,
                onSeeMoreClick = {
                    onSeeMoreClick(
                        null,
                        null,
                        Constants.EVENTS_NEAR_YOU_RADIUS,
                        "distance,asc",
                        null,
                        R.string.near_you
                    )
                },
                onEventClick = onEventClick,
                onEventSaveClick = { it ->
                    onEventSaveClick(it)
                    viewModel.changeEventSaved(
                        id = it.id.toString(),
                        save = !it.saved,
                        eventList = uiState.value.eventsNearYou.data ?: emptyList(),
                        category = 1
                    )
                },
                getImageUrl = { images ->
                    viewModel.getImageUrl(
                        images = images,
                        aspectRatio = "4_3",
                        minImageWidth = 400
                    )
                },
                getDistanceToEvent = { event ->
                    // Gets distance from location
                    val distanceFromLocation = viewModel.getDistanceFromLocation(
                        event.location?.latitude
                            ?: event.embedded?.venues?.get(0)?.location?.latitude,
                        event.location?.longitude
                            ?: event.embedded?.venues?.get(0)?.location?.longitude,
                        DistanceUnit.Miles
                    )

                    distanceFromLocation
                },
                getStartDateTime = { dateData ->
                    viewModel.getFormattedEventStartDates(dateData)
                },
                title = stringResource(R.string.near_you)
            )

            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium_small)))

            // music events
            DiscoverScreenListItem(
                events = uiState.value.musicEvents,
                onSeeMoreClick = {
                    onSeeMoreClick(
                        null,
                        null,
                        null,
                        null,
                        "Music",
                        R.string.music_events
                    )
                },
                onEventClick = onEventClick,
                onEventSaveClick = {
                    onEventSaveClick(it)
                    viewModel.changeEventSaved(
                        id = it.id.toString(),
                        save = !it.saved,
                        eventList = uiState.value.musicEvents.data ?: emptyList(),
                        category = 2
                    )
                },
                getImageUrl = { images ->
                    viewModel.getImageUrl(
                        images = images,
                        aspectRatio = "4_3",
                        minImageWidth = 400
                    )
                },
                getDistanceToEvent = { event ->
                    // Gets distance from location
                    val distanceFromLocation = viewModel.getDistanceFromLocation(
                        event.location?.latitude
                            ?: event.embedded?.venues?.get(0)?.location?.latitude,
                        event.location?.longitude
                            ?: event.embedded?.venues?.get(0)?.location?.longitude,
                        DistanceUnit.Miles
                    )

                    distanceFromLocation
                },
                getStartDateTime = { dateData ->
                    viewModel.getFormattedEventStartDates(dateData)
                },
                title = stringResource(R.string.music_events),
            )

            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium_small)))

            // sports events
            DiscoverScreenListItem(
                events = uiState.value.sportsEvents,
                onSeeMoreClick = {
                    onSeeMoreClick(
                        null,
                        null,
                        null,
                        null,
                        "Sports",
                        R.string.sports_events
                    )
                },
                onEventClick = onEventClick,
                onEventSaveClick = {
                    onEventSaveClick(it)
                    viewModel.changeEventSaved(
                        id = it.id.toString(),
                        save = !it.saved,
                        eventList = uiState.value.sportsEvents.data ?: emptyList(),
                        category = 3
                    )
                },
                getImageUrl = { images ->
                    viewModel.getImageUrl(
                        images = images,
                        aspectRatio = "4_3",
                        minImageWidth = 400
                    )
                },
                getDistanceToEvent = { event ->
                    // Gets distance from location
                    val distanceFromLocation = viewModel.getDistanceFromLocation(
                        event.location?.latitude
                            ?: event.embedded?.venues?.get(0)?.location?.latitude,
                        event.location?.longitude
                            ?: event.embedded?.venues?.get(0)?.location?.longitude,
                        DistanceUnit.Miles
                    )

                    distanceFromLocation
                },
                getStartDateTime = { dateData ->
                    viewModel.getFormattedEventStartDates(dateData)
                },
                title = stringResource(R.string.sports_events)
            )

            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium_small)))

            // arts & theater events
            DiscoverScreenListItem(
                events = uiState.value.artsEvents,
                onSeeMoreClick = {
                    onSeeMoreClick(
                        null,
                        null,
                        null,
                        null,
                        "Arts",
                        R.string.arts_and_theater
                    )
                },
                onEventClick = onEventClick,
                onEventSaveClick = {
                    onEventSaveClick(it)
                    viewModel.changeEventSaved(
                        id = it.id.toString(),
                        save = !it.saved,
                        eventList = uiState.value.artsEvents.data ?: emptyList(),
                        category = 4
                    )
                },
                getImageUrl = { images ->
                    viewModel.getImageUrl(
                        images = images,
                        aspectRatio = "4_3",
                        minImageWidth = 400
                    )
                },
                getDistanceToEvent = { event ->
                    // Gets distance from location
                    val distanceFromLocation = viewModel.getDistanceFromLocation(
                        event.location?.latitude
                            ?: event.embedded?.venues?.get(0)?.location?.latitude,
                        event.location?.longitude
                            ?: event.embedded?.venues?.get(0)?.location?.longitude,
                        DistanceUnit.Miles
                    )

                    distanceFromLocation
                },
                getStartDateTime = { dateData ->
                    viewModel.getFormattedEventStartDates(dateData)
                },
                title = stringResource(R.string.arts_and_theater)
            )

            // looking for something else?
            Column {
                Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)))
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = modifier
                        .clip(MaterialTheme.shapes.medium)
                        .border(
                            3.dp,
                            MaterialTheme.colorScheme.surfaceVariant,
                            MaterialTheme.shapes.medium
                        )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
                    ) {
                        Icon(
                            imageVector = MyIcons.info,
                            contentDescription = null,
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_small)))
                        Text(
                            text = "Looking for something else? Go to the search tab and explore more events!",
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)))

        }
    }
}