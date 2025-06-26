package com.example.concertfinder.presentation.discover_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.concertfinder.R
import com.example.concertfinder.common.Constants
import com.example.concertfinder.common.Resource
import com.example.concertfinder.data.model.Event

@Composable
fun DiscoverScreenListItem(
    events: Resource<List<Event>>,
    onSeeMoreClick: () -> Unit,
    onEventClick: (Event) -> Unit,
    onEventSaveClick: (Event) -> Unit,
    title: String,
    modifier: Modifier = Modifier
) {
    Column(

    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
        ) {
            if (events is Resource.Success) {
                items(events.data ?: emptyList()) { event ->
                    CompactEventListItem(
                        event = event,
                        onEventClick = onEventClick,
                        onEventSaveClick = onEventSaveClick,
                        imageUrl = "TODO", // TODO: add image url
                        distanceToEvent = "TODO", // TODO: add distance to event
                        startDateTime = "TODO", // TODO: add start date and time
                    )
                }
                item {
                    SeeMoreListItem(
                        onClick = onSeeMoreClick,
                    )
                }
            } else if (events is Resource.Loading) {
                items(count = Constants.DISCOVER_PAGE_SIZE.toInt()) {
                    LoadingListItem()
                }
            } else if (events is Resource.Error) {
                item {
                    ErrorListItem(
                        message = events.message ?: "An unexpected error occurred"
                    )
                }
            }
        }
    }
}