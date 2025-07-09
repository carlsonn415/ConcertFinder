package com.example.lineup_app.presentation.event_list_screen

import com.example.lineup_app.common.Resource
import com.example.lineup_app.data.model.Event

data class EventListUiState(
    val eventsResource: Resource<List<Event>> = Resource.Loading(),
    val page: String = "0",
    val isLoadingMore: Boolean = false,
    val canLoadMore: Boolean = true,
)
