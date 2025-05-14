package com.example.concertfinder.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.concertfinder.R
import com.example.concertfinder.model.DateInfo
import com.example.concertfinder.model.Event
import com.example.concertfinder.model.StartDateInfo

@Composable
fun SearchResultsScreen(
    eventList: List<Event>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn {
            items(eventList) { event ->
                SearchResultItem(
                    event = event,
                    onClick = onClick,
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
fun SearchResultItem(
    event: Event,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
         onClick = onClick,
        modifier = modifier.fillMaxWidth().padding(dimensionResource(id = R.dimen.padding_small))
    ) {
        Text(
            text = event.name,
            modifier = modifier.padding(dimensionResource(R.dimen.padding_small))
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchResultsScreenPreview() {
    SearchResultsScreen(
        eventList = listOf(
            Event(
                id = "1",
                name = "Test Event",
                location = Pair(1.0, 1.0),
                imageList = emptyList(),
                dateInfo = DateInfo(StartDateInfo("", "")),
            )
        ),
        onClick = {},
        modifier = Modifier
    )
}