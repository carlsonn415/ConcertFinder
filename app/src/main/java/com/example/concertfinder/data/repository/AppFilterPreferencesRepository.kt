package com.example.concertfinder.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log.e
import androidx.core.content.edit
import com.example.concertfinder.domain.repository.PreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import javax.inject.Inject

class AppFilterPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
): PreferencesRepository.FilterPreferencesRepository {

    // SharedPreferences
    private val prefsName = "filter_prefs"
    private val keyRadius = "radius"
    private val keyStartDateTime = "start_date_time"
    private val keySort = "sort"
    private val keyGenre = "genre"
    private val keySubgenre = "subgenre"
    private val keySegment = "segment"


    // Default values
    private val defaultRadius = "50"
    private val defaultStartDateTime = getFormattedDate(Calendar.getInstance())
    private val defaultSort = "relevance,desc"
    private val defaultGenre = null
    private val defaultSubgenre = null
    private val defaultSegment = null


    override suspend fun saveRadius(radius: String?) {
        if (radius != null) {
            getPrefs(context).edit {
                putString(keyRadius, radius)
            }
        } else {
            e("UserLocationPreferences", "Invalid radius data")
        }
    }

    override fun getRadius(): String {
        return getPrefs(context).getString(keyRadius, defaultRadius) ?: defaultRadius
    }

    override suspend fun saveStartDateTime(startDateTime: String?) {
        if (startDateTime != null) {
            getPrefs(context).edit {
                putString(keyStartDateTime, startDateTime)
            }
        } else {
            e("UserLocationPreferences", "Invalid start date time data")
        }
    }

    override fun getStartDateTime(): String {
        return getPrefs(context).getString(keyStartDateTime, defaultStartDateTime) ?: defaultStartDateTime
    }

    override suspend fun saveSort(sort: String?) {
        if (sort != null) {
            getPrefs(context).edit {
                putString(keySort, sort)
            }
        } else {
            e("UserLocationPreferences", "Invalid sort data")
        }
    }

    override fun getSort(): String {
        return getPrefs(context).getString(keySort, defaultSort) ?: defaultSort
    }

    override suspend fun saveGenre(genre: List<String>?) {
        if (genre != null) {
            getPrefs(context).edit {
                putStringSet(keyGenre, genre.toSet())
            }
        } else {
            e("UserLocationPreferences", "Invalid genre data")
        }
    }

    override fun getGenres(): List<String>? {
        return getPrefs(context).getStringSet(keyGenre, defaultGenre)?.toList()
    }

    override suspend fun removeGenre(genre: String) {
        getPrefs(context).edit {
            val genres = getGenres()?.toMutableList() ?: return
            genres.remove(genre)
            putStringSet(keyGenre, genres.toSet())
        }
    }

    override suspend fun saveSubgenre(subgenre: List<String>?) {
        if (subgenre != null) {
            getPrefs(context).edit {
                putStringSet(keySubgenre, subgenre.toSet())
            }
        } else {
            e("UserLocationPreferences", "Invalid subgenre data")
        }
    }

    override fun getSubgenres(): List<String>? {
        return getPrefs(context).getStringSet(keySubgenre, defaultSubgenre)?.toList()
    }

    override suspend fun removeSubgenre(subgenre: String) {
        getPrefs(context).edit {
            val subgenres = getSubgenres()?.toMutableList() ?: return
            subgenres.remove(subgenre)
            putStringSet(keySubgenre, subgenres.toSet())
        }
    }

    override suspend fun saveSegment(segment: String?) {
        if (segment != null) {
            getPrefs(context).edit {
                putString(keySegment, segment)
            }
        } else {
            e("UserLocationPreferences", "Invalid segment data")
        }
    }

    override fun getSegment(): String? {
        return getPrefs(context).getString(keySegment, defaultSegment) ?: defaultSegment
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

    // get formatted date from calendar
    @SuppressLint("NewApi")
    private fun getFormattedDate(calendar: Calendar): String {
        // convert calendar to instant
        val instant: Instant = calendar.toInstant()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            .withZone(ZoneId.of("UTC"))

        return formatter.format(instant)
    }
}