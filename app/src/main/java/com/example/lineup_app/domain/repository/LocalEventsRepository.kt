package com.example.lineup_app.domain.repository

import com.example.lineup_app.common.Resource
import com.example.lineup_app.data.model.Event
import kotlinx.coroutines.flow.Flow

interface LocalEventsRepository {
    suspend fun saveNewEvent(event: Event)
    suspend fun getEvents(): Flow<Resource<List<Event>>>
    suspend fun deleteEventById(eventId: String)
    suspend fun getEventSavedById(eventId: String): Boolean
    suspend fun getSavedEventsIds(): Set<String>
}