package com.example.concertfinder.model

import com.example.concertfinder.ui.utils.NavigationBarElement

data class ConcertFinderUiState(
    val loadingStatus: LoadingStatus = LoadingStatus.Loading,
    val currentScreen: NavigationBarElement = NavigationBarElement.Events,
    val showBottomBar: Boolean = true,
)
