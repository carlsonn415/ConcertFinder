package com.example.concertfinder.domain.use_case.update_location

import android.util.Log
import com.example.concertfinder.data.remote.LocationManagerService
import com.example.concertfinder.data.repository.preference_repository.AppPreferencesRepository
import javax.inject.Inject

class UpdateLocationUseCase @Inject constructor(
    preferencesRepository: AppPreferencesRepository,
    private val locationManagerService: LocationManagerService
) {
    private val locationPreferencesRepository = preferencesRepository.getLocationPreferences()

    // get current location from location manager, then save to location preferences
    suspend fun onLocationPermissionGranted() {
        try {
            val currentLocation = locationManagerService.getLocation()

            if (currentLocation != null) {
                // save location to location preferences
                locationPreferencesRepository.saveLocation(
                    latitude = currentLocation.latitude,
                    longitude = currentLocation.longitude
                )
            }
        } catch (e: Exception) {
            // Catch any exception from locationManager.getLocation()
            Log.d("Location", e.message.toString())
        }
    }

    suspend fun onLocationSearch(query: String) {
        try {
            if (query.isNotBlank()) {
                locationPreferencesRepository.saveLocation(address = query)
            }
        } catch (e: Exception) {
            Log.d("Location", e.message.toString())
        }
    }

    fun isLocationPermissionGranted(): Boolean {
        return locationManagerService.isLocationPermissionGranted()
    }
}