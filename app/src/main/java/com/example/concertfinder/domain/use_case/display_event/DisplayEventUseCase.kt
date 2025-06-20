package com.example.concertfinder.domain.use_case.display_event

import android.location.Location
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.concertfinder.data.remote.event_dto.DateData
import com.example.concertfinder.data.remote.event_dto.EventImage
import com.example.concertfinder.domain.model.DistanceUnit
import com.example.concertfinder.domain.repository.PreferencesRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject
import kotlin.math.roundToInt

class DisplayEventUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {
    fun getDistanceToEvent(
        latitude: Double,
        longitude: Double,
        unit: DistanceUnit
    ): Double {
        val userLatitude = preferencesRepository.getLocationPreferences().getLatitude()
        val userLongitude = preferencesRepository.getLocationPreferences().getLongitude()

        val distanceInMeters = calculateDistance(
            userLatitude,
            userLongitude,
            latitude,
            longitude,
        )

        if (unit == DistanceUnit.Miles) {
            val distanceInMiles = distanceInMeters / 1609.34 // 1 mile = 1609.34 meters
            return ( distanceInMiles * 10 ).roundToInt() / 10.0 // Round to 1 decimal place
        } else if (unit == DistanceUnit.Kilometers) {
            val distanceInKilometers = distanceInMeters / 1000.0 // 1 kilometer = 1000 meters
            return ( distanceInKilometers * 10 ).roundToInt() / 10.0 // Round to 1 decimal place
        } else {
            return (distanceInMeters * 10).roundToInt() / 10.0
        }
    }

    fun getImageUrl(
        images: List<EventImage>?,
        aspectRatio: String,
        minImageWidth: Int
    ): String? {
        if (images != null && images.isNotEmpty()) {
            val currentImage = images[0]

            for (image in images) {
                if (image.ratio == aspectRatio && (image.width ?: 0) >= minImageWidth) {
                    return image.url
                }
            }
            return currentImage.url
        } else {
            return null
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
                    return formattedStartDateTime
                } else {
                    // formats event start date
                    val formattedStartDate = formatEventDate(dates.start.localDate)
                    return formattedStartDate
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
                            return formattedEndDateTime
                        } else {
                            // formats event end date
                            val formattedEndDate = formatEventDate(dates.end.localDate)
                            return formattedEndDate
                        }
                    } else {
                        // Checks if event has an end time
                        if (dates.end.localTime != null) {
                            // formats event end time
                            val formattedEndTime = formatEventTime(dates.end.localTime)
                            return formattedEndTime
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
            Log.e("DisplayEventUseCase", "Error formatting event date/time", e)
            return "Invalid date or time"
        }

        val localDateTime = LocalDateTime.of(localDate, localTime)

        val dayOfWeekName = localDateTime.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())

        val outputTimeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault())
        val formattedTime = localDateTime.format(outputTimeFormatter)

        val monthName = localDateTime.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        val dayOfMonth = localDateTime.dayOfMonth
        val year = localDateTime.year

        val daySuffix = getDayOfMonthSuffix(dayOfMonth)

        // Checks if event is happening this year, if so does not return year in string
        return if (year != LocalDate.now().year) {
            "$dayOfWeekName, $monthName $dayOfMonth$daySuffix, $year at $formattedTime"
        } else {
            "$dayOfWeekName, $monthName $dayOfMonth$daySuffix at $formattedTime"
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatEventTime(time: String): String {
        val inputTimeFormatter = DateTimeFormatter.ISO_LOCAL_TIME

        val localTime: LocalTime
        try {
            localTime = LocalTime.parse(time, inputTimeFormatter)
        } catch (e: Exception) {
            Log.e("DisplayEventUseCase", "Error formatting event time", e)
            return "Invalid time"
        }

        val outputTimeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault())
        return localTime.format(outputTimeFormatter)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatEventDate(date: String): String {
        val inputDateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

        val localDate: LocalDate
        try {
            localDate = LocalDate.parse(date, inputDateFormatter)
        } catch (e: Exception) {
            Log.e("DisplayEventUseCase", "Error formatting event date", e)
            return "Invalid date"
        }

        val dayOfWeekName = localDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        val monthName = localDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        val dayOfMonth = localDate.dayOfMonth
        val year = localDate.year

        val daySuffix = getDayOfMonthSuffix(dayOfMonth)

        // Checks if event is happening this year, if so does not return year in string
        return if (year != LocalDate.now().year) {
            "$dayOfWeekName, $monthName $dayOfMonth$daySuffix, $year"
        } else {
            "$dayOfWeekName, $monthName $dayOfMonth$daySuffix"
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

    private fun calculateDistance(
        userLatitude: Double,
        userLongitude: Double,
        eventLatitude: Double,
        eventLongitude: Double,
    ): Float {
        val location1 = Location("User")
        location1.latitude = userLatitude
        location1.longitude = userLongitude

        val location2 = Location("Event")
        location2.latitude = eventLatitude
        location2.longitude = eventLongitude

        return location1.distanceTo(location2)

    }
}