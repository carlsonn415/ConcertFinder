package com.example.concertfinder.presentation.common_ui.location_menu

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.concertfinder.domain.model.LoadingStatus
import com.example.concertfinder.domain.repository.PreferencesRepository
import com.example.concertfinder.domain.use_case.update_filter_preference.UpdateFilterPreferenceUseCase
import com.example.concertfinder.domain.use_case.update_location.UpdateLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val updateLocationUseCase: UpdateLocationUseCase,
    private val updateFilterPreferenceUseCase: UpdateFilterPreferenceUseCase,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LocationUiState(
        address = preferencesRepository.getLocationPreferences().getAddress(),
        radius = preferencesRepository.getFilterPreferences().getRadius(),
        locationLoadingStatus = LoadingStatus.Idle
    ))
    val uiState = _uiState.asStateFlow()

    // Event to signal UI to request location permissions
    private val _requestLocationPermissionEvent = MutableSharedFlow<Unit>()
    val requestLocationPermissionEvent = _requestLocationPermissionEvent.asSharedFlow()

    init {
        if (updateLocationUseCase.isLocationPermissionGranted()) {
            updateLocation(isAppStarting = true)
        }
    }

    // get current location from location manager, then save to location preferences
    fun updateLocation(
        isAppStarting: Boolean = false
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            _uiState.update { currentState ->
                currentState.copy(
                    locationLoadingStatus = LoadingStatus.Loading
                )
            }

            try {
                updateLocationUseCase.onLocationPermissionGranted(isAppStarting)
                Log.d("Location", "Location updated")

                // update current address in UI
                _uiState.update { currentState ->
                    currentState.copy(
                        locationLoadingStatus = LoadingStatus.Success,
                        address = preferencesRepository.getLocationPreferences().getAddress()
                    )
                }
            } catch (e: Exception) {

                // Catch any exception from updateLocationUseCase.onLocationPermissionGranted()
                _uiState.update { currentState ->
                    currentState.copy(
                        locationLoadingStatus = LoadingStatus.Error(e.message.toString())
                    )
                }
                Log.d("Location", e.message.toString())
            }
        }
    }

    fun searchForLocation(query: String) {
        viewModelScope.launch(Dispatchers.IO) {

            _uiState.update { currentState ->
                currentState.copy(
                    locationLoadingStatus = LoadingStatus.Loading
                )
            }

            try {
                updateLocationUseCase.onLocationSearch(query)

                // update current address in UI
                _uiState.update { currentState ->
                    currentState.copy(
                        locationLoadingStatus = LoadingStatus.Success,
                        address = preferencesRepository.getLocationPreferences().getAddress()
                    )
                }

            } catch (e: Exception) {
                // Catch any exception from updateLocationUseCase.onLocationSearch()
                _uiState.update { currentState ->
                    currentState.copy(
                        locationLoadingStatus = LoadingStatus.Error(e.message.toString())
                    )
                }

                Log.d("Location", e.message.toString())
            }
        }
    }

    // This function is called by the UI when it wants to initiate a location update.
    // The ViewModel will check if it thinks permission might be needed.
    fun initiateLocationUpdate() {
        viewModelScope.launch {
            _requestLocationPermissionEvent.emit(Unit)
        }
    }

    fun updateRadiusFilterPreference(
        radius: String? = null,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            updateFilterPreferenceUseCase.updateFilterPreferences(
                radius = radius,
            )

            // update radius
            _uiState.update { currentState ->
                currentState.copy(
                    radius = preferencesRepository.getFilterPreferences().getRadius()
                )
            }
        }
    }
}