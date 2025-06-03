package com.example.concertfinder.presentation.search_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.concertfinder.domain.model.LoadingStatus
import com.example.concertfinder.domain.repository.PreferencesRepository
import com.example.concertfinder.domain.repository.SearchHistoryRepository
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
class SearchScreenViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val searchHistoryRepository: SearchHistoryRepository,
    private val updateLocationUseCase: UpdateLocationUseCase,
    private val updateFilterPreferenceUseCase: UpdateFilterPreferenceUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SearchScreenUiState(
            address = preferencesRepository.getLocationPreferences().getAddress(),
            radius = preferencesRepository.getFilterPreferences().getRadius(),
            searchHistory = searchHistoryRepository.getSearchHistory(),
        )
    )
    val uiState = _uiState.asStateFlow()

    // Event to signal UI to request location permissions
    private val _requestLocationPermissionEvent = MutableSharedFlow<Unit>()
    val requestLocationPermissionEvent = _requestLocationPermissionEvent.asSharedFlow()


    // get current location from location manager, then save to location preferences
    fun updateLocation() {
        viewModelScope.launch(Dispatchers.IO) {

            _uiState.update { currentState ->
                currentState.copy(
                    locationLoadingStatus = LoadingStatus.Loading
                )
            }

            try {
                updateLocationUseCase.onLocationPermissionGranted()

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

    fun saveSearchQuery(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchHistoryRepository.saveSearchQuery(query)
            _uiState.update { currentState ->
                currentState.copy(
                    searchHistory = searchHistoryRepository.getSearchHistory()
                )
            }
        }
    }

    fun deletePreviousSearch(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchHistoryRepository.removeSearchQuery(query)
            _uiState.update { currentState ->
                currentState.copy(
                    searchHistory = searchHistoryRepository.getSearchHistory()
                )
            }
        }
    }

    // update search expanded
    fun updateSearchExpanded(expanded: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                isSearchBarExpanded = expanded
            )
        }
    }

    // update search text
    fun updateSearchText(text: String) {
        _uiState.update { currentState ->
            currentState.copy(
                searchQuery = text
            )
        }
    }

    // reset search bar screen
    fun resetSearchBarScreen() {
        _uiState.update { currentState ->
            currentState.copy(
                isSearchBarExpanded = false,
                searchQuery = "",
                isRadiusPreferencesExpanded = false,
                isLocationPreferencesMenuExpanded = false,
                locationSearchQuery = "",
            )
        }
    }

    // update location expanded
    fun updateLocationMenuExpanded(expanded: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                isLocationPreferencesMenuExpanded = expanded
            )
        }
    }

    // update drop down expanded
    fun updateDropDownExpanded(expanded: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                isRadiusPreferencesExpanded = expanded
            )
        }
    }

    // update location search query
    fun updateLocationSearchQuery(query: String) {
        _uiState.update { currentState ->
            currentState.copy(
                locationSearchQuery = query
            )
        }
    }
}