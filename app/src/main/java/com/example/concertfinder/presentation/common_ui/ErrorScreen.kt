package com.example.concertfinder.presentation.common_ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(R.string.no_events_found), style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
                Text(text = stringResource(R.string.try_different_search))
            }
        } else {
            Text(text = "Error: $message")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorScreenPreview() {
    ErrorScreen(message = "No events found")
}