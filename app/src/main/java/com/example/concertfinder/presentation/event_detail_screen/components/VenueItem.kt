package com.example.concertfinder.presentation.event_detail_screen.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.concertfinder.data.remote.event_dto.Venue

@Composable
fun VenueItem(venue: Venue) {
    Text(
        text = "Venue: " + venue.name.toString(),
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(16.dp)
    )
    AsyncImage(
        model = venue.images?.get(0)?.url.toString(),
        contentDescription = "Venue Image",
        modifier = Modifier.padding(16.dp)
    )
    Text(
        text = "Description: " + venue.description.toString(),
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(16.dp)
    )
    Text(
        text = "URL: " + venue.url.toString(),
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(16.dp)
    )
    Text(
        text = "Additional Info: " + venue.additionalInfo.toString(),
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(16.dp)
    )
    Text(
        text = "Parking Detail: " + venue.parkingDetail.toString(),
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(16.dp)
    )
    Text(
        text = "General Info: " + venue.generalInfo?.generalRule.toString() + ", " + venue.generalInfo?.childRule.toString(),
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(16.dp)
    )
    Text(
        text = "Address: " + venue.address?.line1.toString() + ", " + venue.city?.name.toString() + ", " + venue.state?.name.toString(),
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(16.dp)
    )
    Text(
        text = "Location: " + venue.location?.latitude.toString() + ", " + venue.location?.longitude.toString(),
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(16.dp)
    )
    HorizontalDivider()
}