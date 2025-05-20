package com.example.concertfinder.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.util.Log.e
import android.widget.Toast
import androidx.core.content.edit
import com.example.concertfinder.domain.repository.PreferencesRepository.LocationPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.lang.NumberFormatException
import java.util.Locale

class AppLocationPreferencesRepository(
    @ApplicationContext private val context: Context
) : LocationPreferencesRepository {

    // SharedPreferences
    private val prefsName = "user_location_prefs"
    private val keyLatitude = "latitude"
    private val keyLongitude = "longitude"
    private val keyAddress = "address"

    // Default values
    private val defaultLatitude = 40.7128
    private val defaultLongitude = -74.0060
    private val defaultAddress = "New York, NY"


    override suspend fun saveLocation(
        latitude: Double?,
        longitude: Double?,
        address: String?
    ) {
        if (latitude != null && longitude != null) {
            getPrefs(context).edit {
                putString(keyLatitude, latitude.toString())
                putString(keyLongitude, longitude.toString())
            }
            reverseGeocode(context, latitude, longitude)
        } else if (address != null) {

            // remove illegal characters from address
            address.removeIllegalCharacters()
            // Get the latitude and longitude from the address and save
            geocode(context, address)

        } else {
            e("UserLocationPreferences", "Invalid location data")
        }
    }

    override fun getLocation(): String {
        val prefs = getPrefs(context)
        val latString = prefs.getString(keyLatitude, defaultLatitude.toString())
        val longString = prefs.getString(keyLongitude, defaultLongitude.toString())

        return try {
            "$latString,$longString"
        } catch (_: NumberFormatException) {
            "$defaultLatitude,$defaultLongitude"
        }
    }

    override fun getAddress(): String {
        return getPrefs(context).getString(keyAddress, defaultAddress) ?: defaultAddress
    }

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
    }

    private fun reverseGeocode(
        context: Context,
        latitude: Double,
        longitude: Double
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
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "No results found", Toast.LENGTH_SHORT).show() // TODO: Make this a snackbar
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Service not available", Toast.LENGTH_SHORT).show() // TODO: Make this a snackbar
            }
        }
    }

    private fun String.removeIllegalCharacters(): String {
        return this.filter { it.isLetterOrDigit() || it.isWhitespace() }
    }
}