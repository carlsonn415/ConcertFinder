package com.example.lineup_app.presentation.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import com.example.lineup_app.R
import com.example.lineup_app.data.local.AppSnackbarManager
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
            AppSnackbarManager.showSnackbar(context, R.string.location_permission_denied, R.string.settings) {
                // Action to take the user to app settings
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri

                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                } else {
                    AppSnackbarManager.showSnackbar(context, R.string.could_not_open_settings)
                }
            }
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