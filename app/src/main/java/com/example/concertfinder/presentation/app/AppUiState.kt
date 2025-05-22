package com.example.concertfinder.presentation.app

import com.example.concertfinder.data.model.Event


data class AppUiState(
    val showBottomBar: Boolean = true,
    val currentEvent: Event = Event(),
)
