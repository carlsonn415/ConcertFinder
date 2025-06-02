package com.example.concertfinder.presentation.filter_screen

import com.example.concertfinder.data.model.Genre
import com.example.concertfinder.data.model.Segment
import com.example.concertfinder.data.model.Subgenre

data class FilterScreenUiState(
    val isSortMenuExpanded: Boolean = false,
    val isSortOptionsExpanded: Boolean = false,
    val isSortTypeExpanded: Boolean = false,
    val currentSortOption: String = "",
    val currentSortType: String = "",
    val segmentOptions: List<Segment> = emptyList(),
    val genreOptions: List<Genre> = emptyList(),
    val subgenreOptions: List<Subgenre> = emptyList(),
    val currentSegment: String = "",
    val currentGenres: List<String> = emptyList(),
    val currentSubgenres: List<String> = emptyList(),
    val isSegmentPreferencesExpanded: Boolean = false,
    val isGenrePreferencesExpanded: Boolean = false,
    val isSubgenrePreferencesExpanded: Boolean = false
)
