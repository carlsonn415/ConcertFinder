package com.example.lineup_app.presentation.common_ui.elements

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.SphericalUtil
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapItem(
    latitude: Double,
    longitude: Double,
    modifier: Modifier = Modifier,
    eventTitle: String? = "Event Location",
    showMarker: Boolean = true,
    showRadius: Boolean = false,
    radiusInMiles: Double? = null
) {
    val eventLocation = remember(latitude, longitude) { LatLng(latitude, longitude) }
    val cameraPositionState = rememberCameraPositionState {
        // Default position, will be updated if radius is shown
        position = CameraPosition.fromLatLngZoom(
            eventLocation,
            if (showRadius && radiusInMiles == null) 1f else 15f
        ) // Initial zoom and position
    }
    var uiSettings by remember {
        mutableStateOf(MapUiSettings(
            zoomControlsEnabled = true,
            scrollGesturesEnabled = false,
            zoomGesturesEnabled = false,
            rotationGesturesEnabled = false,
            tiltGesturesEnabled = false,
            mapToolbarEnabled = false
        ))
    }
    var mapProperties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL, mapStyleOptions = null))
    }

    val radiusInMeters = radiusInMiles?.times(1609.344)

    // Effect to update camera when radius or lat/long parameters change
    LaunchedEffect(key1 = eventLocation, key2 = radiusInMeters, key3 = showRadius) {
        if (showRadius && radiusInMeters != null && radiusInMeters > 0) {
            val circleBounds = getBoundsForCircle(eventLocation, radiusInMeters)
            // The padding (in pixels) to apply to the bounds when fitting the camera.
            // This ensures the circle isn't right at the edge of the map view.
            val padding = 200
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngBounds(circleBounds, padding),
                durationMs = 500 // Animation duration
            )
        } else if (showRadius && radiusInMeters == null) {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(eventLocation, 1f),
                durationMs = 500 // Animation duration
            )
        } else {
            // If no radius or not shown, just zoom to the event location
            cameraPositionState.position = CameraPosition.fromLatLngZoom(eventLocation, 15f)
        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = mapProperties,
        uiSettings = uiSettings
    ) {
        if (showMarker) {
            Marker(
                state = MarkerState(position = eventLocation),
                title = eventTitle,
                snippet = "Lat: $latitude, Lng: $longitude"
            )
        }

        if (showRadius && radiusInMiles != null && radiusInMiles > 0) {
            Circle(
                center = eventLocation,
                radius = radiusInMiles * 1609.344, // Converts miles to meters
                strokeColor = Color.Blue, // Outline color of the circle
                strokeWidth = 5f,          // Outline width in pixels
                fillColor = Color.Blue.copy(alpha = 0.3f) // Fill color with some transparency
            )
        }
    }
}

private fun getBoundsForCircle(center: LatLng, radiusInMeters: Double): LatLngBounds {
    val southwest = SphericalUtil.computeOffset(center, radiusInMeters, 225.0) // Southwest
    val northeast = SphericalUtil.computeOffset(center, radiusInMeters, 45.0)  // Northeast
    return LatLngBounds(southwest, northeast)
}