package com.example.concertfinder.presentation.saved_events_screen

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.concertfinder.data.remote.event_dto.DateData
import com.example.concertfinder.data.remote.event_dto.EventImage
import com.example.concertfinder.domain.model.DistanceUnit
import com.example.concertfinder.domain.use_case.display_event.DisplayEventUseCase
import com.example.concertfinder.domain.use_case.get_saved_events.GetSavedEventsUseCase
import com.example.concertfinder.domain.use_case.save_event.SaveEventUseCase
import com.example.concertfinder.domain.use_case.unsave_event.UnsaveEventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedEventsViewModel @Inject constructor(
    private val getSavedEventsUseCase: GetSavedEventsUseCase,
    private val unsaveEventUseCase: UnsaveEventUseCase,
    private val saveEventsUseCase: SaveEventUseCase,
    private val displayEventUseCase: DisplayEventUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SavedEventsUiState())
    val uiState: StateFlow<SavedEventsUiState> = _uiState.asStateFlow()


    init {
        getSavedEvents()
    }

    fun getSavedEvents() {
        viewModelScope.launch {
            getSavedEventsUseCase().collect {
                _uiState.update { state ->
                    state.copy(
                        events = it
                    )
                }
            }
        }
    }

    fun getDistanceFromLocation(
        latitude: Double?,
        longitude: Double?,
        unit: DistanceUnit
    ): String {
        return if (latitude != null && longitude != null && unit == DistanceUnit.Miles) {
            "${displayEventUseCase.getDistanceToEvent(latitude, longitude, unit)} mi"
        } else if (latitude != null && longitude != null && unit == DistanceUnit.Kilometers) {
            "${displayEventUseCase.getDistanceToEvent(latitude, longitude, unit)} km"
        } else {
            ""
        }
    }

    @SuppressLint("NewApi")
    fun getFormattedEventStartDates(dates: DateData?): String? {
        return displayEventUseCase.getFormattedEventStartDates(dates)
    }

    fun getImageUrl(
        images: List<EventImage>?,
        aspectRatio: String = "16_9",
        minImageWidth: Int = 1080
    ): String? {
        return displayEventUseCase.getImageUrl(images, aspectRatio, minImageWidth)
    }
}