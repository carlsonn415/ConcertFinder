package com.example.concertfinder.domain.use_case.calculate_distance

import android.location.Location
import com.example.concertfinder.domain.model.DistanceUnit
import com.example.concertfinder.domain.repository.PreferencesRepository
import javax.inject.Inject
import kotlin.math.roundToInt

class CalculateDistanceUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {
    operator fun invoke(
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