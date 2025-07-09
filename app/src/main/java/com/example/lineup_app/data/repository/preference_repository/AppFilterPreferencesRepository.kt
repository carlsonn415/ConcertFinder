package com.example.lineup_app.data.repository.preference_repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.example.lineup_app.common.Constants.DEFAULT_GENRE
import com.example.lineup_app.common.Constants.DEFAULT_RADIUS
import com.example.lineup_app.common.Constants.DEFAULT_SEGMENT
import com.example.lineup_app.common.Constants.DEFAULT_SORT_OPTION
import com.example.lineup_app.common.Constants.DEFAULT_SORT_TYPE
import com.example.lineup_app.common.Constants.DEFAULT_START_DATE_TIME
import com.example.lineup_app.common.Constants.DEFAULT_SUBGENRE
import com.example.lineup_app.domain.repository.PreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppFilterPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
): PreferencesRepository.FilterPreferencesRepository {

    // SharedPreferences
    private val prefsName = "filter_prefs"
    private val keyRadius = "radius"
    private val keyStartDateTime = "start_date_time"
    private val keySortOption = "sort_option"
    private val keySortType = "sort_type"
    private val keyGenre = "genre"
    private val keySubgenre = "subgenre"
    private val keySegment = "segment"

    override suspend fun saveRadius(radius: String?) {
        if (radius != null) {
            getPrefs(context).edit {
                putString(keyRadius, radius)
            }
        } else {
            Log.e("UserLocationPreferences", "Invalid radius data")
        }
    }

    override fun getRadius(): String {
        return getPrefs(context).getString(keyRadius, DEFAULT_RADIUS) ?: DEFAULT_RADIUS
    }

    override suspend fun saveStartDateTime(startDateTime: String?) {
        if (startDateTime != null) {
            getPrefs(context).edit {
                putString(keyStartDateTime, startDateTime)
            }
        } else {
            Log.e("UserLocationPreferences", "Invalid start date time data")
        }
    }

    override fun getStartDateTime(): String {
        return getPrefs(context).getString(keyStartDateTime, DEFAULT_START_DATE_TIME) ?: DEFAULT_START_DATE_TIME
    }

    override suspend fun saveSortOption(sortOption: String?) {
        if (sortOption != null) {
            getPrefs(context).edit {
                putString(keySortOption, sortOption)
            }
        } else {
            Log.e("UserLocationPreferences", "Invalid sort data")
        }
    }

    override fun getSortOption(): String {
        return getPrefs(context).getString(keySortOption, DEFAULT_SORT_OPTION) ?: DEFAULT_SORT_OPTION
    }

    override suspend fun saveSortType(sortType: String?) {
        if (sortType != null) {
            getPrefs(context).edit {
                putString(keySortType, sortType)
            }
        } else {
            Log.e("UserLocationPreferences", "Invalid sort type data")
        }
    }

    override fun getSortType(): String {
        return getPrefs(context).getString(keySortType, DEFAULT_SORT_TYPE) ?: DEFAULT_SORT_TYPE
    }

    override fun getSort(): String {
        return "${getSortOption()},${getSortType()}"
    }

    override suspend fun saveGenre(genre: List<String>?) {
        if (genre != null) {
            val newGenreList = (getGenres()?.toMutableList()?.plus(genre)) ?: mutableListOf()
            getPrefs(context).edit {
                putStringSet(keyGenre, newGenreList.toSet())
            }
        } else {
            Log.e("UserLocationPreferences", "Invalid genre data")
        }
    }

    override fun getGenres(): List<String>? {
        return getPrefs(context).getStringSet(keyGenre, DEFAULT_GENRE)?.toList()
    }

    override suspend fun removeSingleGenre(genre: String) {
        getPrefs(context).edit {
            val genres = getGenres()?.toMutableList() ?: return
            genres.remove(genre)
            putStringSet(keyGenre, genres.toSet())
        }
    }

    override suspend fun removeAllGenres() {
        getPrefs(context).edit {
            remove(keyGenre)
            remove(keySubgenre)
        }
    }

    override suspend fun saveSubgenre(subgenre: List<String>?) {
        if (subgenre != null) {
            val newSubgenreList = (getSubgenres()?.toMutableList()?.plus(subgenre)) ?: mutableListOf()
            getPrefs(context).edit {
                putStringSet(keySubgenre, newSubgenreList.toSet())
            }
        } else {
            Log.e("UserLocationPreferences", "Invalid subgenre data")
        }
    }

    override fun getSubgenres(): List<String>? {
        return getPrefs(context).getStringSet(keySubgenre, DEFAULT_SUBGENRE)?.toList()
    }

    override suspend fun removeSingleSubgenre(subgenre: String) {
        getPrefs(context).edit {
            val subgenres = getSubgenres()?.toMutableList() ?: return
            subgenres.remove(subgenre)
            putStringSet(keySubgenre, subgenres.toSet())
        }
    }

    override suspend fun removeAllSubgenres() {
        getPrefs(context).edit {
            remove(keySubgenre)
        }
    }

    override suspend fun saveSegment(segment: String?) {
        if (segment != null) {
            getPrefs(context).edit {
                putString(keySegment, segment)
            }
        } else {
            Log.e("UserLocationPreferences", "Invalid segment data")
        }
    }

    override fun getSegment(): String? {
        val currentSegment = getPrefs(context).getString(keySegment, DEFAULT_SEGMENT)
        Log.d("AppFilterPreferencesRepository", "currentSegment: $currentSegment")
        return currentSegment
    }

    override suspend fun removeSegment() {
        getPrefs(context).edit(commit = true) {
            remove(keySegment)
            remove(keyGenre)
            remove(keySubgenre)
        }
    }

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
    }
}