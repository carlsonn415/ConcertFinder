package com.example.concertfinder.presentation.saved_events_screen

import com.example.concertfinder.common.Resource
import com.example.concertfinder.data.model.Event

data class SavedEventsUiState(
    val events: Resource<List<Event>> = Resource.Loading(),
)
