package com.example.concertfinder.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.concertfinder.R
import com.example.concertfinder.model.apidata.Event

@Composable
fun SearchResultsScreen(
    eventList: List<Event>,
    onClick: (Event) -> Unit,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(0.dp)
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        if (eventList.isNotEmpty()) {
            LazyColumn {
                items(eventList) { event ->
                    SearchResultItem(
                        event = event,
                        onClick = onClick,
                        modifier = modifier
                    )
                }
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = modifier.fillMaxSize()
            ) {
                Text(
                    text = stringResource(R.string.no_events_found),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = modifier
                        .padding(horizontal = dimensionResource(R.dimen.padding_medium))
                        .padding(bottom = dimensionResource(R.dimen.padding_small))
                )
                Text(
                    text = stringResource(R.string.try_different_search),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    modifier = modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium))
                )
            }
        }
    }
}

@Composable
fun SearchResultItem(
    event: Event,
    onClick: (Event) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onClick(event) },
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_small))
    ) {
        Text(
            text = event.name.toString(),
            modifier = modifier.padding(dimensionResource(R.dimen.padding_small))
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchResultsScreenPreview() {
    SearchResultsScreen(
        eventList = listOf<Event>(),
        onClick = {},
        modifier = Modifier
    )
}