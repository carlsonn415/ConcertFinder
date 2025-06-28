package com.example.concertfinder.presentation.discover_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.concertfinder.R
import com.example.concertfinder.data.model.Event
import com.example.concertfinder.data.remote.event_dto.Address
import com.example.concertfinder.data.remote.event_dto.City
import com.example.concertfinder.data.remote.event_dto.Classification
import com.example.concertfinder.data.remote.event_dto.Genre
import com.example.concertfinder.data.remote.event_dto.Place
import com.example.concertfinder.data.remote.event_dto.PriceRange
import com.example.concertfinder.data.remote.event_dto.Segment
import com.example.concertfinder.data.remote.event_dto.State
import com.example.concertfinder.data.remote.event_dto.SubGenre
import com.example.concertfinder.presentation.common_ui.ClassificationFlowRow
import com.example.concertfinder.presentation.ui.theme.MyIcons

@Composable
fun CompactEventListItem(
    event: Event,
    imageUrl: String?,
    distanceToEvent: String,
    startDateTime: String,
    onEventClick: (Event) -> Unit,
    onEventSaveClick: (Event) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable {
                onEventClick(event)
            }
            .width(dimensionResource(R.dimen.compact_event_list_item_width))
            //.height(dimensionResource(R.dimen.compact_event_list_item_height))
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        //------------------------------------------------------------------------------------------ image box
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier
                .size(dimensionResource(R.dimen.compact_event_list_item_width))
                .clip(MaterialTheme.shapes.medium)
                .background(color = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.image_placeholder),
                error = painterResource(R.drawable.image_placeholder),
                modifier = Modifier.fillMaxSize()
            )

            ClassificationFlowRow(
                classifications = event.classifications ?: listOf(),
                horizontalArrangement = Arrangement.End,
                maxLines = 1,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
            )

            Column {
                IconButton(
                    onClick = {
                        onEventSaveClick(event)
                    },
                ) {
                    Icon(
                        imageVector = if (event.saved) MyIcons.heart else MyIcons.heartOutlined,
                        contentDescription = stringResource(R.string.save_event),
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.icon_size_large))
                            //.padding(dimensionResource(R.dimen.padding_small)),
                    )
                }

                // spacer to push button to top
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        Spacer(modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_small)))

        Row {
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_small)))
            Column {
                //------------------------------------------------------------------------------------------ event name
                Text(
                    text = event.name ?: stringResource(R.string.no_event_name),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.height(40.dp)
                )

                Spacer(modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_extra_small)))

                //------------------------------------------------------------------------------------------ start date and time

                Text(
                    text = startDateTime,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_extra_small)))

                //------------------------------------------------------------------------------------------ starting price and distance
                val price = event.priceRanges?.getOrNull(0)?.min?.toInt()

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_small))
                ) {
                    if (price != null) {
                        Text(
                            text = if (price > 0) "From $${price.toInt()}" else stringResource(R.string.free),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )

                        Box(
                            modifier = Modifier
                                .padding(horizontal = dimensionResource(R.dimen.padding_small))
                                .size(height = 16.dp, width = 1.dp)
                                .background(color = MaterialTheme.colorScheme.primary)
                        )
                    }

                    Text(
                        text = distanceToEvent,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.primary,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CompactEventListItemPreview() {
    CompactEventListItem(
        Event(
            name = "Event Name",
            saved = false,
            description = "Event Description",
            additionalInfo = "Additional Info",
            info = "Info",
            pleaseNote = "Please Note",
            priceRanges = listOf(PriceRange(min = 10.0, max = 20.0)),
            classifications = listOf(Classification(segment = Segment(name = "Music"), genre = Genre("Rock"), subGenre = SubGenre("Pop"))),
            place = Place(Address("123 Main St"), City("City"), State("State")),
        ),
        onEventClick = {},
        onEventSaveClick = {},
        imageUrl = "",
        distanceToEvent = "10.0 mi",
        startDateTime = "Tue, November 23rd at 8:00 PM",
    )
}