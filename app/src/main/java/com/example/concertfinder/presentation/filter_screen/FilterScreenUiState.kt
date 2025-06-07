package com.example.concertfinder.presentation.filter_screen

import com.example.concertfinder.data.model.Genre
import com.example.concertfinder.data.model.Segment
import com.example.concertfinder.data.model.Subgenre
import com.example.concertfinder.domain.model.LoadingStatus

data class FilterScreenUiState(
    val locationLoadingStatus: LoadingStatus = LoadingStatus.Idle,
    val address: String = "",
    val isRadiusPreferencesExpanded: Boolean = false,
    val radius: String = "",
    val isLocationPreferencesMenuExpanded: Boolean = true,
    val locationSearchQuery: String = "",
    val isSortMenuExpanded: Boolean = true,
    val isFilterMenuExpanded: Boolean = true,
    val currentSortOption: String = "",
    val segmentOptions: List<Segment> = emptyList(),
    val genreOptions: List<Genre> = emptyList(),
    val subgenreOptions: List<Subgenre> = emptyList(),
    val currentSegment: String = "",
    val currentGenres: List<String> = emptyList(),
    val currentSubgenres: List<String> = emptyList(),
    val isSegmentPreferencesExpanded: Boolean = false,
    val isGenrePreferencesExpanded: Boolean = false,
    val isSubgenrePreferencesExpanded: Boolean = false,
    val preferencesUpdated: Boolean = false
)
