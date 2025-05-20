package com.example.concertfinder.data.remote

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
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
}

class AppLocationManagerService(
    @ApplicationContext private val context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : LocationManagerService {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getLocation(): Location? {

        val hasGrantedFineLocationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasGrantedCoarseLocationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val locationManager = context.getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager

        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!isGpsEnabled && !(hasGrantedCoarseLocationPermission || hasGrantedFineLocationPermission)) {
            Log.d("Location", "No location permission granted")
            return null
        }

        val cancellationTokenSource = CancellationTokenSource()

        val currentLocationRequest = CurrentLocationRequest.Builder()
            .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
            .setDurationMillis(5000) // Max time to wait for location
            .setMaxUpdateAgeMillis(10000) // How old a cached location can be
            .build()

        return suspendCancellableCoroutine { cont ->

            fusedLocationProviderClient.getCurrentLocation(currentLocationRequest, cancellationTokenSource.token)
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        cont.resume(location) {
                            cancellationTokenSource.cancel()
                        }

                        // TODO: Make this toast a snackbar
                        Toast.makeText(context, "Location updated", Toast.LENGTH_SHORT).show()

                        Log.d("Location", "Location is ${location.latitude}, ${location.longitude}")
                    } else {
                        // Location is null, which can happen if the provider can't get a fix quickly enough
                        // or if location services are disabled.
                        cont.resume(null) {
                            cancellationTokenSource.cancel()
                        }
                        // TODO: Make this toast a snackbar
                        Toast.makeText(context, "Error getting location", Toast.LENGTH_SHORT).show()

                        Log.d("Location", "Location is null")
                    }
                }
                .addOnFailureListener { e ->
                    cont.resumeWithException(e)
                    Log.d("Location", "Error getting location: $e")
                    Toast.makeText(context, "Error getting location: $e", Toast.LENGTH_SHORT).show()
                }

            cont.invokeOnCancellation {
                cancellationTokenSource.cancel()
                Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}