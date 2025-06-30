package com.example.concertfinder.presentation.app

import androidx.annotation.StringRes
import com.example.concertfinder.R
import com.example.concertfinder.data.model.Event

data class AppUiState(
    val showBottomBar: Boolean = true,
    val currentEvent: Event = Event(),
    val showFab: Boolean = false,
    val areFiltersApplied: Boolean = false,
    val savedEventsUpdated: Boolean = false,
    val savedEventsIds: Set<String> = emptySet(),
    @StringRes val topBarTitle: Int = R.string.app_name
)
