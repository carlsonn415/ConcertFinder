package com.example.lineup_app.presentation.event_detail_screen

data class EventDetailUiState (
    val showAdditionalInfo: Boolean = false,
    val isDescriptionExpanded: Boolean = false,
    val isAttractionPageExpanded: Boolean = false,
    val infoQueue: MutableMap<String, String> = mutableMapOf()
)