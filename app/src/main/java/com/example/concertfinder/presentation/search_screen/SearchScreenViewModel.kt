package com.example.concertfinder.presentation.search_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.concertfinder.domain.repository.SearchHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val searchHistoryRepository: SearchHistoryRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SearchScreenUiState(
            searchHistory = searchHistoryRepository.getSearchHistory(),
        )
    )
    val uiState = _uiState.asStateFlow()

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

    // update location search query
    fun updateLocationSearchQuery(query: String) {
        _uiState.update { currentState ->
            currentState.copy(
                locationSearchQuery = query
            )
        }
    }
}