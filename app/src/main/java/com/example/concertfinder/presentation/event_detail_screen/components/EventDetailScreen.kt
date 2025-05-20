package com.example.concertfinder.presentation.event_detail_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.concertfinder.data.model.Event

@Composable
fun EventDetailScreen(
    event: Event,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .padding(innerPadding)
            .padding(top = 16.dp)
    ) {
        Text(
            text = event.name.toString(),
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier
        )
        Text(
            text = event.description.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(top = 16.dp)
        )
        Text(
            text = event.location?.latitude.toString() + ", " + event.location?.longitude.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(top = 16.dp)
        )
        Text(
            text = event.url.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(top = 16.dp)
        )
        Text(
            text = event.dates?.start?.localDate + " " + event.dates?.start?.localTime,
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(top = 16.dp)
        )
        Text(
            text = event.dates?.end?.localDate + " " + event.dates?.end?.localTime,
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(top = 16.dp)
        )
        Text(
            text = event.dates?.timezone.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(top = 16.dp)
        )
        Text(
            text = event.dates?.status?.code.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(top = 16.dp)
        )
        Text(
            text = event.info.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(top = 16.dp)
        )
        Text(
            text = event.pleaseNote.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(top = 16.dp)
        )
        Text(
            text = event.place?.address?.line1.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(top = 16.dp)
        )
        Text(
            text = event.place?.city?.name.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(top = 16.dp)
        )
        Text(
            text = event.place?.state?.name.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(top = 16.dp)
        )
        Text(
            text = event.priceRanges?.get(0)?.currency.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(top = 16.dp)
        )
        Text(
            text = event.priceRanges?.get(0)?.min.toString() + " - " + event.priceRanges?.get(0)?.max.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(top = 16.dp)
        )
        Text(
            text = event.classifications?.get(0)?.segment?.name.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(top = 16.dp)
        )
        Text(
            text = event.classifications?.get(0)?.genre?.name.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(top = 16.dp)
        )
        Text(
            text = event.classifications?.get(0)?.subGenre?.name.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(top = 16.dp)
        )
        Text(
            text = event.embedded?.venues?.get(0)?.name.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(top = 16.dp)
        )
        Text(
            text = event.embedded?.venues?.get(0)?.description.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(top = 16.dp)
        )
        Text(
            text = event.embedded?.venues?.get(0)?.url.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(top = 16.dp)
        )
        Text(
            text = event.embedded?.venues?.get(0)?.address?.line1.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(top = 16.dp)
        )
        Text(
            text = event.embedded?.venues?.get(0)?.city?.name.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(top = 16.dp)
        )
        Text(
            text = event.embedded?.venues?.get(0)?.state?.name.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(top = 16.dp)
        )
        Text(
            text = event.embedded?.venues?.get(0)?.generalInfo?.generalRule.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(top = 16.dp)
        )
        Text(
            text = event.embedded?.venues?.get(0)?.generalInfo?.childRule.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(top = 16.dp)
        )
        Text(
            text = event.embedded?.attractions?.get(0)?.name.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(top = 16.dp)
        )
        Text(
            text = event.embedded?.attractions?.get(0)?.description.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(top = 16.dp)
        )
        Text(
            text = event.embedded?.attractions?.get(0)?.url.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(top = 16.dp)
        )
        Text(
            text = event.embedded?.attractions?.get(0)?.classifications?.get(0)?.segment?.name.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(top = 16.dp)
        )
        Text(
            text = event.embedded?.attractions?.get(0)?.classifications?.get(0)?.genre?.name.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(top = 16.dp)
        )
        Text(
            text = event.embedded?.attractions?.get(0)?.classifications?.get(0)?.subGenre?.name.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier.padding(top = 16.dp)
        )
    }
}

/*
@Preview(showBackground = true)
@Composable
private fun EventDetailsScreenPreview() {
    EventDetailScreen(
        event = Event(
            embedded = EmbeddedEventData(
                venues = listOf(
                    Venue(
                        id = "123",
                        name = "Test Venue",
                        description = "This is a test venue",
                        url = "https://example.com",
                        images = listOf(
                            EventImage(
                                url = "https://example.com/image.jpg",
                                ratio = "16:9",
                            )
                        ),
                        additionalInfo = "Additional info",
                        address = Address(
                            line1 = "123 Main St",
                        ),
                        city = City(
                            name = "Anytown",
                        ),
                        state = State(
                            name = "CA",
                        ),
                        parkingDetail = "Parking info",
                        generalInfo = GeneralInfo(
                            generalRule = "General rule",
                            childRule = "Child rule",
                        )
                    )
                ),
                attractions = listOf(
                    Attraction(
                        id = "123",
                        name = "Test Attraction",
                        images = listOf(
                            EventImage(
                                url = "https://example.com/image.jpg",
                                ratio = "16:9",
                            )
                        ),
                        description = "This is a test attraction",
                        url = "https://example.com",
                        additionalInfo = "Additional info",
                        classifications = listOf(
                            Classification(
                                segment = Segment(
                                    name = "Test Segment",
                                    id = "123",
                                ),
                                genre = Genre(
                                    name = "Test Genre",
                                    id = "123",
                                ),
                                subGenre = SubGenre(
                                    name = "Test SubGenre",
                                    id = "123",
                                ),
                            )
                        ),
                    )
                )
            ),
            id = "123",
            name = "Test Event",
            location = LocationData(
                latitude = 12.34,
                longitude = 56.78
            ),
            url = "https://example.com",
            description = "This is a test event",
            additionalInfo = "Additional info",
            dates = DateData(
                start = StartData(
                    localDate = "2023-09-15",
                    localTime = "10:00:00",
                    dateTBD = false,
                    dateTBA = false,
                    timeTBA = false,
                    noSpecificTime = false
                ),
                end = EndData(
                    localDate = "2023-09-15",
                    localTime = "12:00:00",
                    noSpecificTime = false,
                    approximate = false
                ),
                access = AccessData(
                    startDateTime = "2023-09-15T10:00:00",
                    startApproximate = false,
                ),
                timezone = "America/Los_Angeles",
                status = StatusData(
                    code = Status.Canceled,
                ),
                spanMultipleDays = false,
            ),
            images = listOf(
                EventImage(
                    url = "https://example.com/image.jpg",
                    ratio = "16:9",
                )
            ),
            info = "Info",
            pleaseNote = "Please Note",
            priceRanges = listOf<PriceRange>(
                PriceRange(
                    currency = "USD",
                    min = 10.0,
                    max = 20.0,
                )
            ),
            classifications = listOf<Classification>(
                Classification(
                    segment = Segment(
                        name = "Test Segment",
                        id = "123",
                    ),
                    genre = Genre(
                        name = "Test Genre",
                        id = "123",
                    ),
                    subGenre = SubGenre(
                        name = "Test SubGenre",
                        id = "123",
                    ),
                )
            ),
            place = Place(
                address = Address(
                    line1 = "123 Main St",
                ),
                city = City(
                    name = "Anytown",
                ),
                state = State(
                    name = "CA",
                ),
            )
        )
    )
}

*/