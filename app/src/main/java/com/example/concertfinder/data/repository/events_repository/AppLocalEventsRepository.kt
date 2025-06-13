package com.example.concertfinder.data.repository.events_repository

import com.example.concertfinder.common.Resource
import com.example.concertfinder.data.local.EventDao
import com.example.concertfinder.data.local.entities.event_entities.toAttractionClassificationEntity
import com.example.concertfinder.data.local.entities.event_entities.toAttractionEntity
import com.example.concertfinder.data.local.entities.event_entities.toAttractionImageEntity
import com.example.concertfinder.data.local.entities.event_entities.toEventClassificationEntity
import com.example.concertfinder.data.local.entities.event_entities.toEventEntity
import com.example.concertfinder.data.local.entities.event_entities.toEventImageEntity
import com.example.concertfinder.data.local.entities.event_entities.toVenueEntity
import com.example.concertfinder.data.local.entities.event_entities.toVenueImageEntities
import com.example.concertfinder.data.model.Event
import com.example.concertfinder.domain.repository.LocalEventsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AppLocalEventsRepository @Inject constructor(
    private val eventDao: EventDao
): LocalEventsRepository {

    override suspend fun saveNewEvent(event: Event) {
        eventDao.insertEvent(event.toEventEntity())
        eventDao.insertEventImages(event.images?.map { it.toEventImageEntity(event.id) } ?: emptyList())
        eventDao.insertEventClassifications(event.classifications?.map { it.toEventClassificationEntity(event.id) } ?: emptyList())
        eventDao.insertVenues(event.embedded?.venues?.map { it.toVenueEntity(event.id) } ?: emptyList())
        event.embedded?.venues?.forEach { venue ->
            eventDao.insertVenueImages(venue.toVenueImageEntities())
        }
        eventDao.insertAttractions(event.embedded?.attractions?.map { it.toAttractionEntity(event.id) } ?: emptyList())
        event.embedded?.attractions?.forEach {
            eventDao.insertAttractionClassifications(it.classifications?.map { it.toAttractionClassificationEntity(event.id) } ?: emptyList())
        }
        event.embedded?.attractions?.forEach {
            eventDao.insertVenueImages(it.images?.map { it.toAttractionImageEntity(event.id) } ?: emptyList())
        }
    }

    override suspend fun getEvents(): Flow<Resource<List<Event>>> {
        return flow {
            emit(Resource.Loading())

            val events = eventDao.getAllEventsWithAllDetails().map {
                it.toEvent(
                    venues = eventDao.getVenuesForEvent(it.event.eventId),
                    attractions = eventDao.getAttractionsForEvent(it.event.eventId)
                )
            }
            emit(Resource.Success(events))
        }
    }

    override suspend fun deleteEventById(eventId: String) {
        eventDao.deleteEventById(eventId)
    }
}
