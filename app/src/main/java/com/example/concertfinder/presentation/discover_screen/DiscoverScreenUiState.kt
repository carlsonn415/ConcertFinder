package com.example.concertfinder.presentation.discover_screen

import com.example.concertfinder.common.Resource
import com.example.concertfinder.data.model.Event

data class DiscoverScreenUiState(
    val isRefreshing: Boolean = false,
    val eventsThisWeekend: Resource<List<Event>> = Resource.Loading(),
    val eventsNearYou: Resource<List<Event>> = Resource.Loading(),
    val musicEvents: Resource<List<Event>> = Resource.Loading(),
    val sportsEvents: Resource<List<Event>> = Resource.Loading(),
    val artsEvents: Resource<List<Event>> = Resource.Loading()
)
