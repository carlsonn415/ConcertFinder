package com.example.concertfinder.presentation.event_detail_screen.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.concertfinder.data.remote.event_dto.Attraction

@Composable
fun AttractionItem(attraction: Attraction) {
    Text(
        text = "Attraction: " + attraction.name.toString(),
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(16.dp)
    )
    AsyncImage(
        model = attraction.images?.get(0)?.url.toString(),
        contentDescription = "Attraction Image",
        modifier = Modifier.padding(16.dp)
    )
    Text(
        text = "Description: " + attraction.description.toString(),
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(16.dp)
    )
    Text(
        text = "URL: " + attraction.url.toString(),
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(16.dp)
    )
    Text(
        text = "Additional Info: " + attraction.additionalInfo.toString(),
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(16.dp)
    )
    if (attraction.classifications != null) {
        for (classification in attraction.classifications) {
            Text(
                text = "Classification: " + classification.segment?.name.toString() + ", " + classification.genre?.name.toString() + ", " + classification.subGenre?.name.toString(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
    } else {
        Text(
            text = "No classifications found",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp)
        )
    }
    HorizontalDivider()
}