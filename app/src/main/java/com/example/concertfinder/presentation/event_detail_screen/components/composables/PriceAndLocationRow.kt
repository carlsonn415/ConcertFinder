package com.example.concertfinder.presentation.event_detail_screen.components.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.example.concertfinder.R
import com.example.concertfinder.data.model.Event

@Composable
fun PriceAndLocationRow(
    event: Event,
    distanceFromLocation: String,
    modifier: Modifier = Modifier
) {
    // Event price and location row
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
            .horizontalScroll(rememberScrollState())
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
        if (distanceFromLocation.isNotEmpty()) {
            Text(
                text = distanceFromLocation,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
            )
            Box(
                modifier = modifier
                    .padding(horizontal = dimensionResource(R.dimen.padding_small))
                    .size(height = 16.dp, width = 1.dp)
                    .background(color = MaterialTheme.colorScheme.onBackground)
            )
        }
        // Check if event has location, if not use venue location
        if (
            event.place != null
            && event.place.address != null
            && event.place.city != null
            && event.place.state != null
        ) {
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = null,
                    modifier = modifier.size(14.dp)
                )
                Text(
                    text = event.embedded.venues.first().address?.line1 + ", " + event.embedded.venues.first().city?.name + ", " + event.embedded.venues.first().state?.name,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = modifier.padding(start = dimensionResource(R.dimen.padding_extra_small))
                )
            }
        } else {
            Text(
                text = "Location not provided",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}