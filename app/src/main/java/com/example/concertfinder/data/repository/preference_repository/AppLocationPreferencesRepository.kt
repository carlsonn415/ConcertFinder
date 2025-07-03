package com.example.concertfinder.data.repository.preference_repository

import android.content.Context
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.core.content.edit
import com.example.concertfinder.R
import com.example.concertfinder.common.Constants
import com.example.concertfinder.data.local.AppSnackbarManager
import com.example.concertfinder.domain.repository.PreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import java.util.Locale

class AppLocationPreferencesRepository(
    @ApplicationContext private val context: Context
) : PreferencesRepository.LocationPreferencesRepository {

    // SharedPreferences
    private val prefsName = "user_location_prefs"
    private val keyLatitude = "latitude"
    private val keyLongitude = "longitude"
    private val keyAddress = "address"

    override suspend fun saveLocation(
        latitude: Double?,
        longitude: Double?,
        address: String?,
        isAppStarting: Boolean // if true, don't show snackbar
    ) {
        if (latitude != null && longitude != null) {
            getPrefs(context).edit {
                putString(keyLatitude, latitude.toString())
                putString(keyLongitude, longitude.toString())
            }
            reverseGeocode(context, latitude, longitude, isAppStarting)
        } else if (address != null) {

            // remove illegal characters from address
            address.removeIllegalCharacters()
            // Get the latitude and longitude from the address and save
            geocode(context, address)

        } else {
            Log.e("UserLocationPreferences", "Invalid location data")
        }
    }

    override fun getLocation(): String {
        val prefs = getPrefs(context)
        val latString = prefs.getString(keyLatitude, Constants.DEFAULT_LATITUDE.toString())
        val longString = prefs.getString(keyLongitude, Constants.DEFAULT_LONGITUDE.toString())

        return try {
            "$latString,$longString"
        } catch (_: NumberFormatException) {
            "$Constants.DEFAULT_LATITUDE,$Constants.DEFAULT_LONGITUDE"
        }
    }

    override fun getAddress(): String {
        return getPrefs(context).getString(keyAddress, Constants.DEFAULT_ADDRESS) ?: Constants.DEFAULT_ADDRESS
    }

    override fun getLatitude(): Double {
        return getPrefs(context).getString(keyLatitude, Constants.DEFAULT_LATITUDE.toString())?.toDouble() ?: Constants.DEFAULT_LATITUDE
    }

    override fun getLongitude(): Double {
        return getPrefs(context).getString(keyLongitude, Constants.DEFAULT_LONGITUDE.toString())?.toDouble() ?: Constants.DEFAULT_LONGITUDE
    }

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
    }

    private fun reverseGeocode(
        context: Context,
        latitude: Double,
        longitude: Double,
        isAppStarting: Boolean
    ) {
        val geocoder = Geocoder(context, Locale.getDefault())

        try {
            @Suppress("DEPRECIATION")
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

            if (!addresses.isNullOrEmpty()) {
                val locality = addresses[0].locality
                val adminArea = addresses[0].adminArea

                // Combine the locality, admin area, and postal code into a single string
                val addressString = "$locality, $adminArea"

                getPrefs(context).edit {
                    putString(keyAddress, addressString)
                }

                if (!isAppStarting) {
                    // show snackbar that says location saved
                    AppSnackbarManager.showSnackbar("Location saved: $addressString")
                }
            } else {
                // show snackbar that says no results are found
                if (!isAppStarting) {
                    AppSnackbarManager.showSnackbar(context, R.string.no_results_found)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            Log.d("Location", "Invalid latitude or longitude")
            null
        }
    }

    private suspend fun geocode(
        context: Context,
        addressString: String
    ) {
        val geocoder = Geocoder(context, Locale.getDefault())

        try {
            @Suppress("DEPRECIATION")
            val addresses: List<Address>? = geocoder.getFromLocationName(addressString, 1)
            if (!addresses.isNullOrEmpty()) {
                val latitude = addresses[0].latitude
                val longitude = addresses[0].longitude

                // Save the latitude and longitude, which will then trigger the reverse geocode
                // to save the formatted address string
                saveLocation(latitude, longitude)
            } else {
                // show snackbar that says no results are found
                AppSnackbarManager.showSnackbar(context, R.string.no_results_found)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            AppSnackbarManager.showSnackbar(context, R.string.service_not_available)
        }
    }

    private fun String.removeIllegalCharacters(): String {
        return this.filter { it.isLetterOrDigit() || it.isWhitespace() }
    }
}