package com.example.concertfinder.domain.repository

import com.example.concertfinder.common.Resource
import com.example.concertfinder.data.model.Event
import kotlinx.coroutines.flow.Flow

interface LocalEventsRepository {
    suspend fun saveNewEvent(event: Event)
    suspend fun getEvents(): Flow<Resource<List<Event>>>
    suspend fun deleteEventById(eventId: String)
    suspend fun getEventSavedById(eventId: String): Boolean

}