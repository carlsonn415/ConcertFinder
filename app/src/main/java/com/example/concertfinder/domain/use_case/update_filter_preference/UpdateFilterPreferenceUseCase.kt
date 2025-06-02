package com.example.concertfinder.domain.use_case.update_filter_preference

import android.util.Log
import com.example.concertfinder.domain.repository.PreferencesRepository
import javax.inject.Inject

class UpdateFilterPreferenceUseCase @Inject constructor(
    preferencesRepository: PreferencesRepository
){
    val filterPreferencesRepository = preferencesRepository.getFilterPreferences()

    suspend fun updateFilterPreferences(
        radius: String? = null,
        startDateTime: String? = null,
        sortOption: String? = null,
        sortType: String? = null,
        genre: String? = null,
        subgenre: String? = null,
        segment: String? = null,
    ) {
        if (radius != null) {
            filterPreferencesRepository.saveRadius(radius)
        }
        if (startDateTime != null) {
            filterPreferencesRepository.saveStartDateTime(startDateTime)
        }
        if (sortOption != null) {
            filterPreferencesRepository.saveSortOption(sortOption)
        }
        if (sortType != null) {
            filterPreferencesRepository.saveSortType(sortType)
        }
        if (genre != null) {
            Log.d("UpdateFilterPreferenceUseCase", "genre: $genre")
            filterPreferencesRepository.saveGenre(listOf(genre))
        }
        if (subgenre != null) {
            Log.d("UpdateFilterPreferenceUseCase", "subgenre: $subgenre")
            filterPreferencesRepository.saveSubgenre(listOf(subgenre))
        }
        if (segment != null) {
            Log.d("UpdateFilterPreferenceUseCase", "segment: $segment")
            filterPreferencesRepository.saveSegment(segment)
        }
    }

    suspend fun removeSingleFilterPreference(
        genreIdToRemove: String? = null,
        subgenreIdToRemove: String? = null,
        removeSegment: Boolean = false,
    ) {
        if (genreIdToRemove != null) {
            filterPreferencesRepository.removeGenre(genreIdToRemove)
        }
        if (subgenreIdToRemove != null) {
            filterPreferencesRepository.removeSubgenre(subgenreIdToRemove)
        }
        if (removeSegment) {
            filterPreferencesRepository.removeSegment()
        }
    }

    suspend fun clearSingleFilterPreference(
        clearGenre: Boolean = false,
        clearSubgenre: Boolean = false,
    ) {
        if (clearGenre) {
            filterPreferencesRepository.removeAllGenres()
        }
        if (clearSubgenre) {
            filterPreferencesRepository.removeAllSubgenres()
        }
    }
}