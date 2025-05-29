package com.example.concertfinder.presentation.filter_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.concertfinder.common.Resource
import com.example.concertfinder.domain.use_case.get_classifications_from_network.GetClassificationsFromNetworkUseCase
import com.example.concertfinder.domain.use_case.update_filter_preference.UpdateFilterPreferenceUseCase
import com.example.concertfinder.domain.use_case.update_location.UpdateLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class FilterScreenViewModel @Inject constructor(
    private val updateFilterPreferenceUseCase: UpdateFilterPreferenceUseCase,
    private val updateLocationUseCase: UpdateLocationUseCase,
    private val getClassificationsFromNetworkUseCase: GetClassificationsFromNetworkUseCase
) : ViewModel() {

    // Initialize filter preferences
    private val _uiState = MutableStateFlow(
        FilterScreenUiState(
            currentSortOption = updateFilterPreferenceUseCase.filterPreferencesRepository.getSortOption(),
            currentSortType = updateFilterPreferenceUseCase.filterPreferencesRepository.getSortType()
        )
    )
    val uiState: StateFlow<FilterScreenUiState> = _uiState.asStateFlow()

    fun toggleSortMenuExpanded() {
        _uiState.update { currentState ->
            currentState.copy(
                isSortMenuExpanded = !currentState.isSortMenuExpanded
            )
        }
    }

    fun toggleSortOptionExpanded() {
        _uiState.update { currentState ->
            currentState.copy(
                isSortOptionsExpanded = !currentState.isSortOptionsExpanded
            )
        }
    }

    fun toggleSortTypeExpanded() {
        _uiState.update { currentState ->
            currentState.copy(
                isSortTypeExpanded = !currentState.isSortTypeExpanded
            )
        }
    }

    fun onSortOptionSelected(sortOption: String) {

        if (sortOption == "distance" && updateFilterPreferenceUseCase.filterPreferencesRepository.getSortType() == "desc") {
            onSortTypeSelected("asc")
        }

        viewModelScope.launch(Dispatchers.IO) {
            updateFilterPreferenceUseCase.updateFilterPreferences(sortOption = sortOption)
        }

        _uiState.update { currentState ->
            currentState.copy(
                currentSortOption = sortOption
            )
        }
    }

    fun onSortTypeSelected(sortType: String) {

        if (sortType == "desc" && updateFilterPreferenceUseCase.filterPreferencesRepository.getSortOption() == "distance") {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            updateFilterPreferenceUseCase.updateFilterPreferences(sortType = sortType)
        }

        _uiState.update { currentState ->
            currentState.copy(
                currentSortType = sortType
            )
        }
    }

    fun getClassifications() {
        viewModelScope.launch(Dispatchers.IO) {
            getClassificationsFromNetworkUseCase().collect {
                _uiState.update { currentState ->
                    currentState.copy(
                        loading = true
                    )
                }

                when (it) {
                    is Resource.Success -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                classifications = it.data,
                                loading = false
                            )
                        }
                        Log.d("FilterScreenViewModel", "Success: ${it.data}")
                    }
                    is Resource.Error -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                classifications = null,
                                loading = false
                            )
                        }
                        Log.d("FilterScreenViewModel", "Error: ${it.message}")
                    }
                    is Resource.Loading -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                loading = true
                            )
                        }
                        Log.d("FilterScreenViewModel", "Loading")
                    }
                }
            }
        }
    }
}