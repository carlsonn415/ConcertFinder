package com.example.concertfinder.presentation.common_ui.location_menu

import com.example.concertfinder.domain.model.LoadingStatus

data class LocationUiState(
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val radius: String,
    val locationLoadingStatus: LoadingStatus,
)
