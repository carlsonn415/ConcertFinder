package com.example.concertfinder.domain.use_case.update_filter_preference

import com.example.concertfinder.domain.repository.PreferencesRepository
import javax.inject.Inject

class UpdateFilterPreferenceUseCase @Inject constructor(
    preferencesRepository: PreferencesRepository
){
    val filterPreferencesRepository = preferencesRepository.getFilterPreferences()

    suspend fun updateFilterPreferences(
        radius: String?,
        startDateTime: String?,
        sort: String?,
        genre: String?,
        subgenre: String?,
        segment: String?,
        removeGenre: Boolean,
        removeSubgenre: Boolean,
        removeSegment: Boolean,
    ) {
        if (radius != null) {
            filterPreferencesRepository.saveRadius(radius)
        }
        if (startDateTime != null) {
            filterPreferencesRepository.saveStartDateTime(startDateTime)
        }
        if (sort != null) {
            filterPreferencesRepository.saveSort(sort)
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