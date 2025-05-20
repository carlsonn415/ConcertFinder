package com.example.concertfinder.presentation.event_list_screen

import com.example.concertfinder.common.Resource
import com.example.concertfinder.data.model.Event

data class EventListUiState(
    val events: Resource<List<Event>> = Resource.Loading(),
    val hasLoadedOnce: Boolean = false,
    val currentRadius: String,
    val currentGeoPoint: String,
    val currentStartDateTime: String,
    val currentSort: String,
    val currentKeyWord: String?,
    val currentGenres: List<String>?,
    val currentSubgenres: List<String>?,
    val currentSegment: String?,
)
