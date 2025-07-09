package com.example.lineup_app.presentation.saved_events_screen

import com.example.lineup_app.common.Resource
import com.example.lineup_app.data.model.Event

data class SavedEventsUiState(
    val events: Resource<List<Event>> = Resource.Loading(),
)
