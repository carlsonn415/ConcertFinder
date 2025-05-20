package com.example.concertfinder.presentation.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import com.example.concertfinder.presentation.app.AppViewModel
import kotlinx.coroutines.flow.Flow


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun LaunchLocationPermission(
    context: Context,
    updateLocation: () -> Unit,
    requestLocationPermissionEvent: Flow<Unit>,
) {

    // Launcher for location permission request
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarseLocationGranted =
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (fineLocationGranted || coarseLocationGranted) {
            // Permission Granted
            updateLocation()
        } else {
            // TODO: Make this toast a snackbar with an action to launch settings
            Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    // Collect the event from ViewModel to trigger permission request
    LaunchedEffect(Unit) {
        requestLocationPermissionEvent.collect {
            Log.d("Location", "requestLocationPermissionEvent triggered")
            // Check current permission status before launching
            val fineLocationPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            val coarseLocationPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )

            if (fineLocationPermission == PackageManager.PERMISSION_GRANTED ||
                coarseLocationPermission == PackageManager.PERMISSION_GRANTED
            ) {
                // Permission already granted
                updateLocation()
            } else {
                Log.d("Location", "requesting location permission")
                // Permission not granted, request it
                locationPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }
}