package com.example.concertfinder.local

import android.content.Context
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import androidx.core.content.edit
import java.io.IOException
import java.lang.NumberFormatException
import java.util.Locale

interface LocationPreferences {
    fun saveLocation(latitude: Double, longitude: Double)
    fun getLocation(): String
    fun getAddress(): String
}

class UserLocationPreferences(
    private val context: Context
) : LocationPreferences {
    // SharedPreferences
    private val prefsName = "user_location_prefs"
    private val keyLatitude = "latitude"
    private val keyLongitude = "longitude"
    private val address = "address"

    // Default values
    private val defaultLatitude = 40.7128
    private val defaultLongitude = -74.0060
    private val defaultAddress = "New York, NY"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
    }

    override fun saveLocation(latitude: Double, longitude: Double) {
        getPrefs(context).edit() {
            putString(keyLatitude, latitude.toString())
            putString(keyLongitude, longitude.toString())
        }
        reverseGeocode(context, latitude, longitude)
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
        return getPrefs(context).getString(address, defaultAddress) ?: defaultAddress
    }

    private fun reverseGeocode(
        context: Context,
        latitude: Double,
        longitude: Double
    ) {
        val geocoder = Geocoder(context, Locale.getDefault())

        try {
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val locality = addresses[0].locality
                val adminArea = addresses[0].adminArea

                // Combine the locality, admin area, and postal code into a single string
                val addressString = "$locality, $adminArea"

                getPrefs(context).edit() {
                    putString(address, addressString)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            null
        }
    }
}