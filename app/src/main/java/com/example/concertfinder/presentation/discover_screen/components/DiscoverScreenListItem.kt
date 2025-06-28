package com.example.concertfinder.presentation.discover_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.concertfinder.R
import com.example.concertfinder.common.Constants
import com.example.concertfinder.common.Resource
import com.example.concertfinder.data.model.Event
import com.example.concertfinder.data.remote.event_dto.DateData
import com.example.concertfinder.data.remote.event_dto.EventImage


@Composable
fun DiscoverScreenListItem(
    events: Resource<List<Event>>,
    onSeeMoreClick: () -> Unit,
    onEventClick: (Event) -> Unit,
    onEventSaveClick: (Event) -> Unit,
    getImageUrl: (List<EventImage>) -> String?,
    getDistanceToEvent: (Event) -> String,
    getStartDateTime: (DateData?) -> String,
    title: String,
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    onSeeMoreClick()
                }
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier
                    .padding(vertical = dimensionResource(id = R.dimen.padding_small))
            )

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_small)))

            Text(
                text = stringResource(R.string.see_more),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = modifier
                    .widthIn(max = 100.dp) // Ensures the Icon doesn't get cut off
            )
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_small)))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = stringResource(R.string.see_more),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(dimensionResource(R.dimen.icon_size_medium)).fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))

        if (events is Resource.Error) {
            ErrorListItem(
                message = events.message ?: "Unknown error",
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
            ) {
                if (events is Resource.Success) {
                    items(events.data ?: emptyList()) { event ->
                        CompactEventListItem(
                            event = event,
                            onEventClick = onEventClick,
                            onEventSaveClick = onEventSaveClick,
                            imageUrl = getImageUrl(event.images ?: emptyList()),
                            distanceToEvent = getDistanceToEvent(event),
                            startDateTime = getStartDateTime(event.dates),
                        )
                    }
                } else if (events is Resource.Loading) {
                    items(count = Constants.DISCOVER_PAGE_SIZE.toInt()) {
                        LoadingListItem()
                    }
                }
            }
        }
    }
}