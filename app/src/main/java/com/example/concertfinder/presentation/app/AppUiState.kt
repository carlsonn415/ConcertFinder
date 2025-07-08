package com.example.concertfinder.presentation.app

import androidx.annotation.StringRes
import com.example.concertfinder.R
import com.example.concertfinder.data.model.Event

data class AppUiState(
    val showBottomBar: Boolean = true,
    val currentEvent: Event = Event(),
    val showFab: Boolean = false,
    val areFiltersAppliedFlag: Boolean = false,
    val savedEventsScreenReloadSavedEventsFlag: Boolean = false,
    val discoverScreenReloadSavedEventsFlag: Boolean = false,
    val eventListScreenReloadSavedEventsFlag: Boolean = false,
    val eventListScreenLoadNewEventsFlag: Boolean = true,
    val locationChangedFlag: Boolean = false,
    val savedEventsIds: Set<String> = emptySet(),
    @StringRes val topBarTitleStack: List<Int> = listOf(R.string.empty_string),
)
