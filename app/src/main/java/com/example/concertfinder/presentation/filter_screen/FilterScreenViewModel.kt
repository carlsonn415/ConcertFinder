package com.example.concertfinder.presentation.filter_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.concertfinder.common.Resource
import com.example.concertfinder.domain.use_case.get_classifications.GetClassificationsUseCase
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
    private val getClassificationsUseCase: GetClassificationsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FilterScreenUiState())
    val uiState: StateFlow<FilterScreenUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val shouldFetchNewClassifications =
                false // TODO: Check if new classifications need to be fetched
            if (shouldFetchNewClassifications) {
                // Fetch new classifications from API
                getClassificationsUseCase.saveNewClassifications()
            }

            getSegments()

            // fetch current segment
            val currentSegment =
                updateFilterPreferenceUseCase.filterPreferencesRepository.getSegment()
            // initialize ui state with current segment
            _uiState.update { currentState ->
                currentState.copy(
                    // find segment name from segment id
                    currentSegment = currentState.segmentOptions.find { it.id == currentSegment }?.name
                        ?: ""
                )
            }
            // if current segment is not empty, fetch genres
            if (currentSegment != null) {

                getGenres(currentSegment)

                // fetch current genre names
                val currentGenreNames = mutableListOf<String>()
                for (genreId in updateFilterPreferenceUseCase.filterPreferencesRepository.getGenres()
                    ?: emptyList()) {
                    currentGenreNames.add(
                        uiState.value.genreOptions.find { it.id == genreId }?.name ?: ""
                    )
                }
                // initialize ui state with current genre names
                _uiState.update { currentState ->
                    currentState.copy(
                        currentGenres = currentGenreNames
                    )
                }

                // if current genres are not empty, fetch subgenres
                if (uiState.value.currentGenres.isNotEmpty()) {

                    getSubgenres(updateFilterPreferenceUseCase.filterPreferencesRepository.getGenres())

                    // fetch current subgenre names
                    val currentSubgenreNames = mutableListOf<String>()
                    for (subgenreId in updateFilterPreferenceUseCase.filterPreferencesRepository.getSubgenres()
                        ?: emptyList()) {
                        currentSubgenreNames.add(
                            uiState.value.subgenreOptions.find { it.id == subgenreId }?.name ?: ""
                        )
                    }
                    // initialize ui state with current subgenre names
                    _uiState.update { currentState ->
                        currentState.copy(
                            currentSubgenres = currentSubgenreNames
                        )
                    }
                }
            }
            // update ui state with current sort options
            _uiState.update { currentState ->
                currentState.copy(
                    currentSortOption = updateFilterPreferenceUseCase.filterPreferencesRepository.getSortOption(),
                    currentSortType = updateFilterPreferenceUseCase.filterPreferencesRepository.getSortType(),
                )
            }
        }
    }

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

    fun toggleSegmentPreferencesExpanded() {
        _uiState.update { currentState ->
            currentState.copy(
                isSegmentPreferencesExpanded = !currentState.isSegmentPreferencesExpanded
            )
        }
    }

    fun toggleGenrePreferencesExpanded() {
        _uiState.update { currentState ->
            currentState.copy(
                isGenrePreferencesExpanded = !currentState.isGenrePreferencesExpanded
            )
        }
    }

    fun toggleSubgenrePreferencesExpanded() {
        _uiState.update { currentState ->
            currentState.copy(
                isSubgenrePreferencesExpanded = !currentState.isSubgenrePreferencesExpanded
            )
        }
    }

    fun onSegmentSelected(segmentName: String) {
        // find segment id from segment name
        val newSegmentId = uiState.value.segmentOptions.find { it.name == segmentName }?.id

        // update filter preferences with new segment
        viewModelScope.launch(Dispatchers.IO) {
            updateFilterPreferenceUseCase.updateFilterPreferences(segment = newSegmentId)
        }

        // update ui state with new genre options
        viewModelScope.launch(Dispatchers.IO) {
            getGenres(newSegmentId)
        }
        // clear subgenre options and update ui state with new segment
        Log.d("FilterScreenViewModel", "onSegmentSelected: $segmentName")
        _uiState.update { currentState ->
            currentState.copy(
                currentSegment = segmentName,
                subgenreOptions = emptyList()
            )
        }
    }

    fun onGenreSelected(genreName: String) {
        // find genre id from genre name
        val newGenreId = uiState.value.genreOptions.find { it.name == genreName }?.id
        Log.d("ViewModelDEBUG", "Genre selected. Name: $genreName, ID to save: $newGenreId")

        // create new list of selected genres
        val newGenreList = updateFilterPreferenceUseCase.filterPreferencesRepository.getGenres()?.toMutableList()
        newGenreList?.add(newGenreId ?: "")

        // update filter preferences with new selected genre
        viewModelScope.launch(Dispatchers.IO) {
            updateFilterPreferenceUseCase.updateFilterPreferences(genre = newGenreId)
        }

        // update ui state with new subgenre options
        viewModelScope.launch(Dispatchers.IO){
            getSubgenres(newGenreList)
        }
        // update ui state with new genre
        _uiState.update { currentState ->
            currentState.copy(
                currentGenres = currentState.currentGenres + genreName
            )
        }
    }

    fun onSubgenreSelected(subgenreName: String) {
        // find subgenre id from subgenre name
        val newSubgenreId = uiState.value.subgenreOptions.find { it.name == subgenreName }?.id

        // update filter preferences with new subgenre
        viewModelScope.launch(Dispatchers.IO) {
            updateFilterPreferenceUseCase.updateFilterPreferences(subgenre = newSubgenreId)
        }

        // update ui state with new subgenre
        _uiState.update { currentState ->
            currentState.copy(
                currentSubgenres = currentState.currentSubgenres + subgenreName
            )
        }
    }

    fun clearSegmentPreferences() {
        // clear filter preferences
        viewModelScope.launch(Dispatchers.IO) {
            updateFilterPreferenceUseCase.removeSingleFilterPreference(removeSegment = true)
        }
        // clear ui state
        _uiState.update { currentState ->
            currentState.copy(
                currentSegment = "",
                currentGenres = emptyList(),
                currentSubgenres = emptyList(),
                genreOptions = emptyList(),
                subgenreOptions = emptyList()
            )
        }
    }

    fun clearGenrePreferences() {
        // clear filter preferences
        viewModelScope.launch(Dispatchers.IO) {
            updateFilterPreferenceUseCase.clearSingleFilterPreference(clearGenre = true)
        }
        // clear ui state
        _uiState.update { currentState ->
            currentState.copy(
                currentGenres = emptyList(),
                currentSubgenres = emptyList(),
                subgenreOptions = emptyList()
            )
        }
    }

    fun clearSubgenrePreferences() {
        // clear filter preferences
        viewModelScope.launch(Dispatchers.IO) {
            updateFilterPreferenceUseCase.clearSingleFilterPreference(clearSubgenre = true)
        }
        // clear ui state
        _uiState.update { currentState ->
            currentState.copy(
                currentSubgenres = emptyList()
            )
        }
    }

    private suspend fun getSegments() {
        // Fetch segment list from local data source
        getClassificationsUseCase.getSegments().collect {

            when (it) {
                is Resource.Success -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            segmentOptions = it.data ?: throw Exception("Segment options are null"),
                        )
                    }
                    Log.d("FilterScreenViewModel", "Success: ${it.data}")
                }
                is Resource.Error -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            segmentOptions = emptyList(),
                        )
                    }
                    Log.d("FilterScreenViewModel", "Error: ${it.message}")
                }
                is Resource.Loading -> {
                    Log.d("FilterScreenViewModel", "Loading segments")
                }
            }
        }
    }

    private suspend fun getGenres(segmentId: String? = null) {
        if (segmentId != null) {
            // Fetch genres from repository
            getClassificationsUseCase.getGenres(segmentId).collect {

                when (it) {
                    is Resource.Success -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                genreOptions = it.data ?: throw Exception("Genre options are null"),
                            )
                        }
                        Log.d("FilterScreenViewModel", "Success: ${it.data}")
                    }

                    is Resource.Error -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                genreOptions = emptyList(),
                            )
                        }
                        Log.d("FilterScreenViewModel", "Error: ${it.message}")
                    }

                    is Resource.Loading -> {
                        Log.d("FilterScreenViewModel", "Loading genres")
                    }
                }
            }
        }
    }

    private suspend fun getSubgenres(genreIdList: List<String>? = null) {
        if (genreIdList != null) {
            // Fetch subgenres from repository
            getClassificationsUseCase.getSubgenres(genreIdList).collect {

                when (it) {
                    is Resource.Success -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                subgenreOptions = it.data
                                    ?: throw Exception("Subgenre options are null"),
                            )
                        }
                        Log.d("FilterScreenViewModel", "Success: ${it.data}")
                    }

                    is Resource.Error -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                subgenreOptions = emptyList(),
                            )
                        }
                        Log.d("FilterScreenViewModel", "Error: ${it.message}")
                    }

                    is Resource.Loading -> {
                        Log.d("FilterScreenViewModel", "Loading subgenres")
                    }
                }
            }

        }
    }
}