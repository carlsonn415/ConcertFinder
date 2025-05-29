package com.example.concertfinder.presentation.filter_screen

import com.example.concertfinder.data.model.Classification

data class FilterScreenUiState(
    val isSortMenuExpanded: Boolean = false,
    val isSortOptionsExpanded: Boolean = false,
    val isSortTypeExpanded: Boolean = false,
    val currentSortOption: String = "",
    val currentSortType: String = "",
    val classifications: List<Classification>? = null,
    val loading: Boolean = false
)
