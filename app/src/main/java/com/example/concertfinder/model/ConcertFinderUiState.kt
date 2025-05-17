package com.example.concertfinder.model

data class ConcertFinderUiState(
    val loadingStatus: LoadingStatus = LoadingStatus.Idle,
    val showBottomBar: Boolean = true,
    val searchExpanded: Boolean = false,
    val searchText: String = "",
    val searchResults: List<Event> = emptyList(),
    val currentAddress: String = ""
)
