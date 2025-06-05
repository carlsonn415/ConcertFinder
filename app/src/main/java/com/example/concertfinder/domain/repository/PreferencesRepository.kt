package com.example.concertfinder.domain.repository


interface PreferencesRepository {

    interface LocationPreferencesRepository {
        suspend fun saveLocation(
            latitude: Double? = null,
            longitude: Double? = null,
            address: String? = null
        )

        fun getLocation(): String
        fun getAddress(): String
        fun getLatitude(): Double
        fun getLongitude(): Double
    }

    fun getLocationPreferences(): LocationPreferencesRepository


    interface FilterPreferencesRepository {
        suspend fun saveRadius(radius: String? = null)
        fun getRadius(): String

        suspend fun saveStartDateTime(startDateTime: String? = null)
        fun getStartDateTime(): String

        suspend fun saveSortOption(sortOption: String? = null)
        fun getSortOption(): String

        suspend fun saveSortType(sortType: String? = null)
        fun getSortType(): String

        fun getSort(): String

        suspend fun saveGenre(genre: List<String>? = null)
        fun getGenres(): List<String>?
        suspend fun removeSingleGenre(genre: String)
        suspend fun removeAllGenres()

        suspend fun saveSubgenre(subgenre: List<String>? = null)
        fun getSubgenres(): List<String>?
        suspend fun removeSingleSubgenre(subgenre: String)
        suspend fun removeAllSubgenres()

        suspend fun saveSegment(segment: String? = null)
        fun getSegment(): String?
        suspend fun removeSegment()
    }

    fun getFilterPreferences(): FilterPreferencesRepository
}