package com.example.concertfinder.model.uistate

import com.example.concertfinder.model.apidata.Event

data class SearchScreenUiState(
    val isSearchBarExpanded: Boolean = false,
    val searchQuery: String = "",
    val isLocationPreferencesMenuExpanded: Boolean = false,
    val isRadiusPreferencesExpanded: Boolean = false,
    val searchResults: List<Event> = emptyList(),
    val locationSearchQuery: String = "",
)