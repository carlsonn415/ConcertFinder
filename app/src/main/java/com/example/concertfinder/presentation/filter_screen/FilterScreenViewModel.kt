package com.example.concertfinder.presentation.filter_screen

import androidx.lifecycle.ViewModel
import com.example.concertfinder.domain.use_case.update_filter_preference.UpdateFilterPreferenceUseCase
import com.example.concertfinder.domain.use_case.update_location.UpdateLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class FilterScreenViewModel @Inject constructor(
    updateFilterPreferenceUseCase: UpdateFilterPreferenceUseCase,
    updateLocationUseCase: UpdateLocationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FilterScreenUiState())
    val uiState: StateFlow<FilterScreenUiState> = _uiState.asStateFlow()


}