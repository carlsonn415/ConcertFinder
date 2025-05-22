package com.example.concertfinder.presentation.event_detail_screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class EventDetailViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(EventDetailUiState())
    val uiState: StateFlow<EventDetailUiState> = _uiState.asStateFlow()

    fun toggleDisplayAdditionalInfo() {
        _uiState.update { currentState ->
            currentState.copy(
                showAdditionalInfo = !currentState.showAdditionalInfo
            )
        }
    }
}