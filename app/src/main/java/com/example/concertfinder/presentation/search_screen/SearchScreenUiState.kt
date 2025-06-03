package com.example.concertfinder.presentation.search_screen

import com.example.concertfinder.domain.model.LoadingStatus


data class SearchScreenUiState(
    val locationLoadingStatus: LoadingStatus = LoadingStatus.Idle,
    val address: String,
    val radius: String,
    val isSearchBarExpanded: Boolean = false,
    val searchQuery: String = "",
    val isLocationPreferencesMenuExpanded: Boolean = false,
    val isRadiusPreferencesExpanded: Boolean = false,
    val locationSearchQuery: String = "",
    val searchHistory: List<String> = emptyList(),
)