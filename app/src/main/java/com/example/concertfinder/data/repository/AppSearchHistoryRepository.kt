package com.example.concertfinder.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.concertfinder.domain.repository.SearchHistoryRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AppSearchHistoryRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : SearchHistoryRepository {

    private val prefsName = "search_history"
    private val keyName = "search_history_list"
    private val maxHistorySize = 10

    override fun getSearchHistory(): List<String> {
        val jsonHistory = getPrefs(context).getString(keyName, null)
        return if (jsonHistory != null) {
            val type = object : TypeToken<List<String>>() {}.type
            Gson().fromJson(jsonHistory, type)
        } else {
            emptyList()
        }
    }

    override suspend fun saveSearchQuery(query: String) {

        if (query.isBlank()) {
            return
        }

        val currentHistory = getSearchHistory().toMutableList()

        // Remove if already exists to move it to the top
        currentHistory.removeAll { it.equals(query, ignoreCase = true) }

        // Add to the beginning (most recent)
        currentHistory.add(0, query)

        // Trim if exceeds max size
        val updatedHistory = if (currentHistory.size > maxHistorySize) {
            currentHistory.subList(0, maxHistorySize)
        } else {
            currentHistory
        }

        saveSearchHistory(updatedHistory)
    }

    override suspend fun removeSearchQuery(query: String) {
        val currentHistory = getSearchHistory().toMutableList()

        // Remove query
        currentHistory.removeAll { it.equals(query, ignoreCase = true) }

        saveSearchHistory(currentHistory)
    }

    override suspend fun clearSearchHistory() {
        val prefs = getPrefs(context)

        prefs.edit { remove(keyName) }
    }

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
    }

    private fun saveSearchHistory(history: List<String>) {
        val jsonHistory = Gson().toJson(history)
        getPrefs(context).edit { putString(keyName, jsonHistory) }
    }
}