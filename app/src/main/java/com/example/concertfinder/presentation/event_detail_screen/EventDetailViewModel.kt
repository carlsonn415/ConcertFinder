package com.example.concertfinder.presentation.event_detail_screen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.concertfinder.data.remote.event_dto.DateData
import com.example.concertfinder.data.remote.event_dto.EventImage
import com.example.concertfinder.domain.model.DistanceUnit
import com.example.concertfinder.domain.use_case.display_event.DisplayEventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(
    private val displayEventUseCase: DisplayEventUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventDetailUiState())
    val uiState: StateFlow<EventDetailUiState> = _uiState.asStateFlow()

    fun toggleDisplayAdditionalInfo() {
        _uiState.update { currentState ->
            currentState.copy(
                showAdditionalInfo = !currentState.showAdditionalInfo
            )
        }
    }

    fun toggleDescriptionExpanded() {
        _uiState.update { currentState ->
            currentState.copy(
                isDescriptionExpanded = !currentState.isDescriptionExpanded
            )
        }
    }

    fun toggleAttractionPageExpanded() {
        _uiState.update { currentState ->
            currentState.copy(
                isAttractionPageExpanded = !currentState.isAttractionPageExpanded
            )
        }
    }

    fun updateInfoQueue(infoQueue: MutableMap<String, String>) {
        _uiState.update { currentState ->
            currentState.copy(
                infoQueue = infoQueue
            )
        }
    }

    fun getImageUrl(
        images: List<EventImage>?,
        aspectRatio: String = "16_9",
        minImageWidth: Int = 1080
    ): String? {
        return displayEventUseCase.getImageUrl(images, aspectRatio, minImageWidth)
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun getFormattedEventStartDates(dates: DateData?): String? {
        return displayEventUseCase.getFormattedEventStartDates(dates)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getFormattedEventEndDates(dates: DateData?): String? {
        return displayEventUseCase.getFormattedEventEndDates(dates)
    }
}