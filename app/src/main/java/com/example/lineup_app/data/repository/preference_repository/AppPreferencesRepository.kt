package com.example.lineup_app.data.repository.preference_repository

import android.content.Context
import com.example.lineup_app.domain.repository.PreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : PreferencesRepository {

    private val locationPreferences by lazy {
        AppLocationPreferencesRepository(context)
    }

    override fun getLocationPreferences(): PreferencesRepository.LocationPreferencesRepository {
        return locationPreferences
    }

    private val filterPreferences by lazy {
        AppFilterPreferencesRepository(context)
    }

    override fun getFilterPreferences(): PreferencesRepository.FilterPreferencesRepository {
        return filterPreferences
    }
}