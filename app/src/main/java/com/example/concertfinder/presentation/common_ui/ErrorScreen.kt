package com.example.concertfinder.presentation.common_ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.concertfinder.R

@Composable
fun ErrorScreen(
    message: String
) {
    Box(
        contentAlignment = Alignment.Companion.Center,
        modifier = Modifier.Companion.fillMaxSize()
    ) {
        if (message == "No events found") {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            ) {
                Text(
                    text = stringResource(R.string.no_events_found),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
                Text(
                    text = stringResource(R.string.try_different_search),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        } else if (message == "No saved events found") {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            ) {
                Text(
                    text = stringResource(R.string.no_saved_events),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
                Text(
                    text = stringResource(R.string.find_saved_events),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            ) {
                Text(
                    text = stringResource(R.string.error),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorScreenPreview() {
    ErrorScreen(message = "No events found")
}