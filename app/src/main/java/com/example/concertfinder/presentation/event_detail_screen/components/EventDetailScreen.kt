package com.example.concertfinder.presentation.event_detail_screen.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.concertfinder.R
import com.example.concertfinder.data.model.Event
import com.example.concertfinder.data.remote.event_dto.Attraction
import com.example.concertfinder.data.remote.event_dto.EmbeddedEventData
import com.example.concertfinder.data.remote.event_dto.LocationData
import com.example.concertfinder.data.remote.event_dto.PriceRange
import com.example.concertfinder.data.remote.event_dto.Venue
import com.example.concertfinder.presentation.event_detail_screen.EventDetailViewModel

@Composable
fun EventDetailScreen(
    event: Event,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: EventDetailViewModel = viewModel()
) {

    val uiState = viewModel.uiState.collectAsState()
    
    Box(
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(innerPadding)
                // Blur the background if additional info is displayed
                .then(
                    if (uiState.value.showAdditionalInfo) {
                        Modifier
                            .blur(
                                radiusX = 16.dp,
                                radiusY = 16.dp,
                                edgeTreatment = BlurredEdgeTreatment.Unbounded
                            )
                            .background(color = MaterialTheme.colorScheme.background)
                    } else {
                        Modifier
                    }
                )
        ) {
            // Event image
            AsyncImage(
                model = event.images?.get(0)?.url.toString(),
                contentDescription = "Event Image",
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .fillMaxWidth()
                    .heightIn(min = 140.dp, max = 200.dp)
                    .background(color = MaterialTheme.colorScheme.secondary)
            )

            // Event name
            Text(
                text = event.name.toString(),
                style = MaterialTheme.typography.headlineLarge,
                modifier = modifier
                    .padding(
                        horizontal = dimensionResource(id = R.dimen.padding_medium),
                        vertical = dimensionResource(id = R.dimen.padding_small)
                    )
            )

            // Event price and location row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
                    .padding(bottom = dimensionResource(id = R.dimen.padding_small))
            ) {
                // Check if event has price ranges
                if (
                    event.priceRanges != null
                    && event.priceRanges.first().currency != null
                    && event.priceRanges.first().min != null
                    && event.priceRanges.first().max != null
                ) {
                    if (event.priceRanges.first().currency == "USD") {
                        // USD
                        Text(
                            text = "$" + event.priceRanges.first().min?.toInt()
                                .toString() + " - $" + event.priceRanges.first().max?.toInt()
                                .toString(),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    } else {
                        // TODO: add support for other currencies
                        Text(
                            text = event.priceRanges.first().min.toString() + " " +
                                    event.priceRanges.first().currency.toString() + " - " +
                                    event.priceRanges.first().max.toString() + " " +
                                    event.priceRanges.first().currency.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                    Box(
                        modifier = modifier
                            .padding(horizontal = dimensionResource(R.dimen.padding_small))
                            .size(height = 16.dp, width = 1.dp)
                            .background(color = MaterialTheme.colorScheme.onBackground)
                    )
                }
                // Check if event has location, if not use venue location
                if (event.place != null && event.place.address != null && event.place.city != null && event.place.state != null) {
                    Text(
                        text = event.place.address.line1 + ", " + event.place.city.name + ", " + event.place.state,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                } else if (
                    event.embedded?.venues != null
                    && event.embedded.venues.first().address != null
                    && event.embedded.venues.first().city != null
                    && event.embedded.venues.first().state != null
                ) {
                    Text(
                        text = event.embedded.venues.first().address?.line1 + ", " + event.embedded.venues.first().city?.name + ", " + event.embedded.venues.first().state?.name,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }

            // Event url button
            if (event.url != null) {
                UrlButton(
                    context = LocalContext.current,
                    content = "Get Tickets",
                    url = event.url.toString(),
                    modifier = modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium))
                )
            }

            // Displays most event info in order of importance, if there is undisplayed info it will be accessible in the button below
            if (event.description == null) {
                if (event.info == null) {
                    if (event.additionalInfo == null) {
                        if (event.pleaseNote == null) {
                            Text(
                                text = "No details found",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = modifier.padding(
                                    start = dimensionResource(R.dimen.padding_medium),
                                    bottom = dimensionResource(R.dimen.padding_small)
                                )
                            )
                        } else {
                            TextBlock(
                                title = "Please note",
                                text = event.pleaseNote.toString(),
                                modifier = modifier.padding(start = dimensionResource(R.dimen.padding_medium))
                            )
                        }
                    } else {
                        TextBlock(
                            title = "Event info",
                            text = event.additionalInfo.toString(),
                            modifier = modifier.padding(start = dimensionResource(R.dimen.padding_medium))
                        )
                    }
                } else {
                    TextBlock(
                        title = "Event info",
                        text = event.info.toString(),
                        modifier = modifier.padding(start = dimensionResource(R.dimen.padding_medium))
                    )
                }
            } else {
                TextBlock(
                    title = "Event description",
                    text = event.description.toString(),
                    modifier = modifier.padding(start = dimensionResource(R.dimen.padding_medium))
                )
            }

            InfoButton(
                text = "Additional info",
                onClick = {
                    viewModel.toggleDisplayAdditionalInfo()
                },
                modifier = modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium))
            )


            Text(
                text = "Info: " + event.info.toString(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = modifier.padding(16.dp)
            )
            Text(
                text = "Additional Info: " + event.additionalInfo.toString(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = modifier.padding(16.dp)
            )
            Text(
                text = "Please Note: " + event.pleaseNote.toString(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = modifier.padding(16.dp)
            )
            Text(
                text = "Location: " + event.location?.latitude.toString() + ", " + event.location?.longitude.toString(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = modifier.padding(16.dp)
            )
            Text(
                text = "Date: " + event.dates?.start?.localDate.toString() + " " + event.dates?.start?.localTime.toString(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = modifier.padding(16.dp)
            )
            Text(
                text = "Status: " + event.dates?.status?.code.toString(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = modifier.padding(16.dp)
            )
            Text(
                text = "Place: " + event.place?.address?.line1.toString() + ", " + event.place?.city?.name.toString() + ", " + event.place?.state?.name.toString(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = modifier.padding(16.dp)
            )
            if (event.classifications != null) {
                for (classification in event.classifications) {
                    Text(
                        text = "Classification: " + classification.segment?.name.toString() + ", " + classification.genre?.name.toString() + ", " + classification.subGenre?.name.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = modifier.padding(16.dp)
                    )
                }
            } else {
                Text(
                    text = "No classifications found",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = modifier.padding(16.dp)
                )
            }

            Text(
                text = "Attractions:",
                style = MaterialTheme.typography.titleMedium,
                modifier = modifier.padding(16.dp)
            )
            HorizontalDivider()

            Column {
                for (attraction in event.embedded?.attractions ?: emptyList()) {
                    AttractionItem(attraction = attraction)
                }
            }

            Text(
                text = "Venues:",
                style = MaterialTheme.typography.titleMedium,
                modifier = modifier.padding(16.dp)
            )
            HorizontalDivider()

            Column {
                for (venue in event.embedded?.venues ?: emptyList()) {
                    VenueItem(venue = venue)
                }
            }
        }
        if (uiState.value.showAdditionalInfo) {

            BackHandler {
                viewModel.toggleDisplayAdditionalInfo()
            }

            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .padding(24.dp)
                    .background(color = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                Box(
                    modifier = modifier.padding(16.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Column(
                        modifier = modifier.verticalScroll(rememberScrollState())
                    ) {
                        Row {
                            TextBlock(
                                title = "Event information",
                                text = event.info.toString(),
                                modifier = modifier.padding(start = dimensionResource(R.dimen.padding_medium))
                            )
                            Spacer(modifier = modifier.weight(1f))
                        }
                        Row {
                            TextBlock(
                                title = "Additional information",
                                text = event.additionalInfo.toString(),
                                modifier = modifier.padding(start = dimensionResource(R.dimen.padding_medium))
                            )
                            Spacer(modifier = modifier.weight(1f))
                        }
                        Row {
                            TextBlock(
                                title = "Please note:",
                                text = event.pleaseNote.toString(),
                                modifier = modifier.padding(start = dimensionResource(R.dimen.padding_medium))
                            )
                            Spacer(modifier = modifier.weight(1f))
                        }
                        Spacer(modifier = modifier.height(32.dp))
                    }
                    Row(
                        modifier = modifier.background(color = MaterialTheme.colorScheme.surfaceContainer),
                    ) {
                        Spacer(modifier = modifier.weight(1f))
                        Text(
                            text = stringResource(R.string.close),
                            style = MaterialTheme.typography.labelLarge,
                            textDecoration = TextDecoration.Underline,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = modifier
                                .clickable {
                                    viewModel.toggleDisplayAdditionalInfo()
                                }
                                .padding(
                                    horizontal = dimensionResource(R.dimen.padding_medium),
                                    vertical = dimensionResource(R.dimen.padding_small)
                                )
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EventDetailsScreenPreview() {
    EventDetailScreen(
        event = Event(
            name = "Test Event",
            description = "This is a test event",
            info = "This is a test event",
            additionalInfo = "This is a test event",
            url = "https://www.test.com",
            pleaseNote = "This is a test event",
            priceRanges = listOf(
                PriceRange(
                    min = 10.0,
                    max = 20.0,
                    currency = "USD"
                )
            ),
            location = LocationData(
                latitude = 37.7749,
                longitude = -122.4194
            ),
            dates = null,
            embedded = EmbeddedEventData(
                attractions = listOf(
                    Attraction(
                        name = "Test Attraction",
                        description = "This is a test attraction",
                        url = "https://www.test.com",
                        images = listOf(
                            com.example.concertfinder.data.remote.event_dto.EventImage(
                                url = "https://www.test.com/image.jpg"
                            )
                        ),
                        additionalInfo = "This is a test attraction",
                        classifications = listOf(
                            com.example.concertfinder.data.remote.event_dto.Classification(
                                segment = com.example.concertfinder.data.remote.event_dto.Segment(
                                    name = "Test Segment"
                                ),
                                genre = com.example.concertfinder.data.remote.event_dto.Genre(
                                    name = "Test Genre"
                                ),
                                subGenre = com.example.concertfinder.data.remote.event_dto.SubGenre(
                                    name = "Test SubGenre"
                                )
                            )
                        )
                    )
                ),
                venues = listOf(
                    Venue(
                        name = "Test Venue",
                        description = "This is a test venue",
                        url = "https://www.test.com",
                        images = listOf(
                            com.example.concertfinder.data.remote.event_dto.EventImage(
                                url = "https://www.test.com/image.jpg"
                            )
                        ),
                        additionalInfo = "This is a test venue",
                        location = LocationData(
                            latitude = 37.7749,
                            longitude = -122.4194
                        ),
                        parkingDetail = "This is a test venue",
                        generalInfo = com.example.concertfinder.data.remote.event_dto.GeneralInfo(
                            generalRule = "This is a test venue",
                            childRule = "This is a test venue"
                        ),
                        address = com.example.concertfinder.data.remote.event_dto.Address(
                            line1 = "123 Test St"
                        ),
                        city = com.example.concertfinder.data.remote.event_dto.City(
                            name = "Test City"
                        ),
                        state = com.example.concertfinder.data.remote.event_dto.State(
                            name = "Test State"
                        )
                    )
                )
            )
        )
    )
}