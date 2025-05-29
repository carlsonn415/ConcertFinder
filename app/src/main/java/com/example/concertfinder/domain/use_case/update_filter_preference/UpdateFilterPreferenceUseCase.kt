package com.example.concertfinder.domain.use_case.update_filter_preference

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
        removeGenre: Boolean = false,
        removeSubgenre: Boolean = false,
        removeSegment: Boolean = false,
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
        if (genre != null && removeGenre == false) {
            filterPreferencesRepository.saveGenre(listOf(genre))
        }
        if (subgenre != null && removeSubgenre == false) {
            filterPreferencesRepository.saveSubgenre(listOf(subgenre))
        }
        if (segment != null && removeSegment == false) {
            filterPreferencesRepository.saveSegment(segment)
        }
        if (removeGenre == true) {
            filterPreferencesRepository.removeGenre(genre!!)
        }
        if (removeSubgenre == true) {
            filterPreferencesRepository.removeSubgenre(subgenre!!)
        }
        if (removeSegment == true) {
            filterPreferencesRepository.removeSegment()
        }
    }
}