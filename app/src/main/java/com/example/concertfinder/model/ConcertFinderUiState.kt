package com.example.concertfinder.model

import com.example.concertfinder.ui.utils.ConcertFinderScreen
import com.example.concertfinder.ui.utils.TopLevelRoute
import com.example.concertfinder.ui.utils.topLevelRoutes

data class ConcertFinderUiState(
    val loadingStatus: LoadingStatus = LoadingStatus.Loading,
    val showBottomBar: Boolean = true,
    val searchExpanded: Boolean = false,
    val searchText: String = "",
    val searchResults: List<Event> = emptyList(),
)
