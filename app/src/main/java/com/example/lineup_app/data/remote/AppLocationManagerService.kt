package com.example.lineup_app.data.remote

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.lineup_app.R
import com.example.lineup_app.data.local.AppSnackbarManager
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

interface LocationManagerService {
    suspend fun getLocation(): Location?
    fun isLocationPermissionGranted(): Boolean
}

// Implementation of LocationManagerService.
class AppLocationManagerService(
    @ApplicationContext private val context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : LocationManagerService { // Implements the LocationManagerService interface.

    // Opt-in for experimental coroutines API, specifically for suspendCancellableCoroutine.
    @OptIn(ExperimentalCoroutinesApi::class)
    // Overrides the getLocation function from the interface.
    override suspend fun getLocation(): Location? {

        // Checks if ACCESS_FINE_LOCATION permission is granted.
        val hasGrantedFineLocationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        // Checks if ACCESS_COARSE_LOCATION permission is granted.
        val hasGrantedCoarseLocationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        // Gets the system's LocationManager service.
        val locationManager = context.getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager

        // Checks if GPS or Network location providers are enabled on the device.
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        // If location services are not enabled AND neither coarse nor fine location permission is granted,
        // log a message and return null (no location can be fetched).
        if (!isGpsEnabled && !(hasGrantedCoarseLocationPermission || hasGrantedFineLocationPermission)) {
            Log.d("Location", "No location permission granted or GPS not enabled")
            return null
        }

        // Creates a CancellationTokenSource to allow cancelling the location request.
        val cancellationTokenSource = CancellationTokenSource()

        // Builds a request for the current location.
        val currentLocationRequest = CurrentLocationRequest.Builder()
            .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY) // Sets the desired accuracy and power consumption.
            .setDurationMillis(10000) // Sets a timeout for the location request (max time to wait).
            .setMaxUpdateAgeMillis(10000) // Sets how old a cached location can be before requesting a new one.
            .build() // Creates the CurrentLocationRequest object.

        // Uses suspendCancellableCoroutine to bridge the callback-based Google Play Services API with Kotlin coroutines.
        // This allows awaiting the location result in a suspending way and handles cancellation.
        return suspendCancellableCoroutine { cont -> // 'cont' is the Continuation object.

            // Requests the current location using the FusedLocationProviderClient.
            fusedLocationProviderClient.getCurrentLocation(currentLocationRequest, cancellationTokenSource.token)
                .addOnSuccessListener { location: Location? -> // Callback for successful location retrieval.
                    if (location != null) { // If location is successfully obtained.
                        // Resumes the coroutine with the obtained location.
                        // The block passed to resume is an invokeOnCancellation handler specifically for this resume call.
                        cont.resume(location) {
                            cancellationTokenSource.cancel() // Cancel the token if the coroutine is cancelled after resume.
                        }

                        // Logs the obtained latitude and longitude.
                        Log.i("Location", "Location is ${location.latitude}, ${location.longitude}")
                    } else { // If location is null (e.g., provider can't get a fix, services disabled).
                        // Resumes the coroutine with null, indicating location is not available.
                        cont.resume(null) {
                            cancellationTokenSource.cancel() // Cancel the token if coroutine cancelled.
                        }
                        // Shows a snackbar indicating an error in getting location.
                        AppSnackbarManager.showSnackbar(context, R.string.error_getting_location)

                        // Logs that the location is null.
                        Log.d("Location", "Location is null")
                    }
                }
                .addOnFailureListener { e -> // Callback for failure in location retrieval.
                    cont.resumeWithException(e) // Resumes the coroutine with the encountered exception.
                    Log.e("Location", "Error getting location: $e") // Logs the error.
                    // Shows a message with the error.
                    AppSnackbarManager.showSnackbar(context, R.string.error_getting_location)
                }

            // Sets up a callback for when the coroutine is cancelled.
            cont.invokeOnCancellation {
                cancellationTokenSource.cancel() // Cancels the CancellationTokenSource, stopping the location request.
                Log.i("Location", "Location request cancelled")
            }
        }
    }

    override fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }
}