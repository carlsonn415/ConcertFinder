package com.example.lineup_app.presentation.discover_screen

import com.example.lineup_app.common.Resource
import com.example.lineup_app.data.model.Event

data class DiscoverScreenUiState(
    val isRefreshing: Boolean = false,
    val eventsThisWeekend: Resource<List<Event>> = Resource.Loading(),
    val eventsNearYou: Resource<List<Event>> = Resource.Loading(),
    val musicEvents: Resource<List<Event>> = Resource.Loading(),
    val sportsEvents: Resource<List<Event>> = Resource.Loading(),
    val artsEvents: Resource<List<Event>> = Resource.Loading()
)
