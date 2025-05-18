package com.example.concertfinder.model.uistate

import com.example.concertfinder.model.LoadingStatus
import com.example.concertfinder.model.apidata.Event

data class ConcertFinderUiState(
    val loadingStatus: LoadingStatus = LoadingStatus.Idle,
    val showBottomBar: Boolean = true,
    val searchRadius: String = "50",
    val currentAddress: String = "",
    val currentEvent: Event? = null,
)