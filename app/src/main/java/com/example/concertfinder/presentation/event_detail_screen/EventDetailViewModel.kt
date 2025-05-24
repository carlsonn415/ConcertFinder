package com.example.concertfinder.presentation.event_detail_screen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.concertfinder.data.remote.event_dto.DateData
import com.example.concertfinder.data.remote.event_dto.EventImage
import com.example.concertfinder.domain.model.DistanceUnit
import com.example.concertfinder.domain.use_case.calculate_distance.CalculateDistanceUseCase
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
    private val calculateDistanceUseCase: CalculateDistanceUseCase,
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

    fun toggleVenuePageExpanded() {
        _uiState.update { currentState ->
            currentState.copy(
                isVenuePageExpanded = !currentState.isVenuePageExpanded
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

    fun getImageUrl(images: List<EventImage>?): String? {
        if (images != null) {
            var currentImage = images[0]

            for (image in images) {
                if (image.ratio == "16_9" && (image.width ?: 0) >= 1080) {
                    currentImage = image
                }
            }

            return currentImage.url
        } else {
            return null
        }
    }

    fun getDistanceFromLocation(
        latitude: Double?,
        longitude: Double?,
        unit: DistanceUnit
    ): String {
        return if (latitude != null && longitude != null && unit == DistanceUnit.Miles) {
            "${calculateDistanceUseCase(latitude, longitude, unit)} mi"
        } else if (latitude != null && longitude != null && unit == DistanceUnit.Kilometers) {
            "${calculateDistanceUseCase(latitude, longitude, unit)} km"
        } else {
            ""
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getFormattedEventStartDates(dates: DateData?): String? {

        // Checks if event has any date info
        if (dates != null) {

            // Checks if event date is TBA or TBD
            if (dates.start?.dateTBA == true || dates.start?.dateTBD == true) {
                return "Event date has yet to be announced"
            }

            // Checks if event has a start date and time
            if (dates.start != null && dates.start.localDate != null) {

                if (dates.start.localTime != null) {
                    // formats event start date and time
                    val formattedStartDateTime = formatEventDateTime(dates.start.localDate, dates.start.localTime)
                    return "Start: $formattedStartDateTime"
                } else {
                    // formats event start date
                    val formattedStartDate = formatEventDate(dates.start.localDate)
                    return "Start: $formattedStartDate"
                }
            }
            // returns null if event has no start date info
            return null
        }
        // returns null if event has no date info
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getFormattedEventEndDates(dates: DateData?): String? {

        // Checks if event has any date info
        if (dates != null) {

            // Checks if event has end date info
            if (dates.end != null) {
                // Checks if event ends at an approximate time or no specific time
                if (dates.end.approximate == true || dates.end.noSpecificTime == true) {
                    // returns null if event ends at an approximate time or no specific time
                    return null
                }
                // Checks if event has a start date and time
                if (dates.end.localDate != null) {

                    // Checks if event spans multiple days
                    if (dates.spanMultipleDays == true) {
                        // Checks if event has an end time
                        if (dates.end.localTime != null) {
                            // formats event end date and time
                            val formattedEndDateTime = formatEventDateTime(dates.end.localDate, dates.end.localTime)

                            // returns start and end date and time
                            return "End: $formattedEndDateTime"
                        } else {
                            // formats event end date
                            val formattedEndDate = formatEventDate(dates.end.localDate)
                            return "End: $formattedEndDate"
                        }
                    } else {
                        // Checks if event has an end time
                        if (dates.end.localTime != null) {
                            // formats event end time
                            val formattedEndTime = formatEventTime(dates.end.localTime)
                            return "End: $formattedEndTime"
                        // returns null if event has no end time info
                        } else {
                            return null
                        }
                    }
                // returns null if event has no end date and time info
                } else {
                    return null
                }
            // returns null if event has no end date info
            } else {
                return null
            }
        // returns null if event has no date info
        } else {
            return null
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatEventDateTime(date: String, time: String): String {
        val inputDateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
        val inputTimeFormatter = DateTimeFormatter.ISO_LOCAL_TIME

        val localDate: LocalDate
        val localTime: LocalTime
        try {
            localDate = LocalDate.parse(date, inputDateFormatter)
            localTime = LocalTime.parse(time, inputTimeFormatter)
        } catch (e: Exception) {
            Log.e("EventDetailViewModel", "Error formatting event dates", e)
            return "Invalid date or time"
        }

        val localDateTime = LocalDateTime.of(localDate, localTime)

        val outputTimeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault())
        val formattedTime = localDateTime.format(outputTimeFormatter)

        val monthName = localDateTime.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        val dayOfMonth = localDateTime.dayOfMonth
        val year = localDateTime.year

        val daySuffix = getDayOfMonthSuffix(dayOfMonth)

        // Checks if event is happening this year, if so does not return year in string
        return if (year != LocalDate.now().year) {
            "$monthName $dayOfMonth$daySuffix, $year at $formattedTime"
        } else {
            "$monthName $dayOfMonth$daySuffix at $formattedTime "
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatEventTime(time: String): String {
        val inputTimeFormatter = DateTimeFormatter.ISO_LOCAL_TIME

        val localTime: LocalTime
        try {
            localTime = LocalTime.parse(time, inputTimeFormatter)
        } catch (e: Exception) {
            Log.e("EventDetailViewModel", "Error formatting event time", e)
            return "Invalid time"
        }

        val outputTimeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault())
        return localTime.format(outputTimeFormatter)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatEventDate(date: String): String {
        val inputDateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

        val localDate: LocalDate
        try {
            localDate = LocalDate.parse(date, inputDateFormatter)
        } catch (e: Exception) {
            Log.e("EventDetailViewModel", "Error formatting event date", e)
            return "Invalid date"
        }

        val monthName = localDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        val dayOfMonth = localDate.dayOfMonth
        val year = localDate.year

        val daySuffix = getDayOfMonthSuffix(dayOfMonth)

        // Checks if event is happening this year, if so does not return year in string
        return if (year != LocalDate.now().year) {
            "$monthName $dayOfMonth$daySuffix, $year"
        } else {
            "$monthName $dayOfMonth$daySuffix"
        }
    }

    private fun getDayOfMonthSuffix(dayOfMonth: Int): String {
        if (dayOfMonth in 11..13) {
            return "th"
        }
        return when (dayOfMonth % 10) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }
    }
}