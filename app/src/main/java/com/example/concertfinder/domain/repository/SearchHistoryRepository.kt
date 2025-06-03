package com.example.concertfinder.domain.repository

interface SearchHistoryRepository {
    suspend fun saveSearchQuery(query: String)
    fun getSearchHistory(): List<String>
    suspend fun removeSearchQuery(query: String)
    suspend fun clearSearchHistory()
}