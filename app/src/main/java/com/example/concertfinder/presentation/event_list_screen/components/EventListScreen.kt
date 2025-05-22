package com.example.concertfinder.presentation.event_list_screen.components

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.concertfinder.R
import com.example.concertfinder.common.Resource
import com.example.concertfinder.data.model.Event
import com.example.concertfinder.presentation.common_ui.ErrorScreen
import com.example.concertfinder.presentation.common_ui.LoadingScreen
import com.example.concertfinder.presentation.common_ui.NoEventsFoundScreen
import com.example.concertfinder.presentation.event_list_screen.EventListViewModel

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun EventListScreen(
    onClick: (Event) -> Unit,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: EventListViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState.collectAsState()

    // get events from view model when screen is loaded
    LaunchedEffect(Unit) {
        viewModel.getEvents()
    }

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        if (uiState.value.events is Resource.Success) {
            if (uiState.value.events.data?.isEmpty() == true) {
                NoEventsFoundScreen(modifier = modifier)
            } else {
                LazyColumn {
                    items(uiState.value.events.data ?: emptyList()) { event ->
                        SearchResultItem(
                            event = event,
                            onClick = {
                                onClick(event)
                            },
                            modifier = modifier
                        )
                    }
                }
            }
        } else if (uiState.value.events is Resource.Loading) {
            LoadingScreen(modifier = modifier)
        } else if (uiState.value.events is Resource.Error) {
            ErrorScreen(message = uiState.value.events.message ?: "Unknown error")
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

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Preview(showBackground = true)
@Composable
private fun EventListScreenPreview() {
    EventListScreen(
        onClick = {},
        modifier = Modifier
    )
}