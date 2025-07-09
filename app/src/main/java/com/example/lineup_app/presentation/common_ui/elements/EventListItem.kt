package com.example.lineup_app.presentation.common_ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import coil.compose.AsyncImage
import com.example.lineup_app.R
import com.example.lineup_app.data.model.Event
import com.example.lineup_app.data.remote.event_dto.Address
import com.example.lineup_app.data.remote.event_dto.City
import com.example.lineup_app.data.remote.event_dto.Classification
import com.example.lineup_app.data.remote.event_dto.Genre
import com.example.lineup_app.data.remote.event_dto.Place
import com.example.lineup_app.data.remote.event_dto.PriceRange
import com.example.lineup_app.data.remote.event_dto.Segment
import com.example.lineup_app.data.remote.event_dto.State
import com.example.lineup_app.data.remote.event_dto.SubGenre
import com.example.lineup_app.presentation.ui.theme.MyIcons

@Composable
fun EventListItem(
    event: Event,
    onClick: (Event) -> Unit,
    onClickSave: (Event) -> Unit,
    distanceToEvent: String,
    startDateTime: String,
    imageUrl: String?,
    modifier: Modifier = Modifier,
) {
    //---------------------------------------------------------------------------------------------- event list item
    Column(
        modifier = modifier.clickable {
            onClick(event)
        }
    ) {
        //------------------------------------------------------------------------------------------ image box
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.event_list_item_height))
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
        }

        Spacer(modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_small)))

        //------------------------------------------------------------------------------------------ name and save button
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = event.name ?: stringResource(R.string.no_event_name),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = {
                    onClickSave(event)
                },
            ) {
                Icon(
                    imageVector = if (event.saved) MyIcons.heart else MyIcons.heartOutlined,
                    contentDescription = stringResource(R.string.save_event)
                )
            }
        }

        //------------------------------------------------------------------------------------------ start date and distance
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = startDateTime,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = dimensionResource(R.dimen.padding_small))
                    .size(height = 16.dp, width = 1.dp)
                    .background(color = MaterialTheme.colorScheme.onBackground)
            )

            Text(
                text = distanceToEvent,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Spacer(modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_extra_small)))

        //------------------------------------------------------------------------------------------ starting price
        val price = event.priceRanges?.getOrNull(0)?.min?.toInt()

        if (price != null) {
            Text(
                text = if (price > 0) "From $${price.toInt()}" else stringResource(R.string.free),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

//-------------------------------------------------------------------------------------------------- preview
@Preview(showBackground = true)
@Composable
private fun EventListItemPreview() {
    EventListItem(
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
        onClickSave = { _ -> },
        onClick = {},
        imageUrl = "",
        distanceToEvent = "10.0 mi",
        startDateTime = "July 23rd at 8PM",
    )
}