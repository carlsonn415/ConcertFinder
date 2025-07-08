package com.example.concertfinder.data.repository.events_repository

import android.util.Log
import com.example.concertfinder.common.Resource
import com.example.concertfinder.data.local.EventDao
import com.example.concertfinder.data.local.entities.event_entities.EventAttractionCrossRef
import com.example.concertfinder.data.local.entities.event_entities.EventVenueCrossRef
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
        // Insert the event entity first
        eventDao.insertEvent(event.toEventEntity())
        // Insert the event's images and classifications
        eventDao.insertEventImages(event.images?.map { it.toEventImageEntity(event.id ?: throw Exception("Event ID cannot be null")) } ?: emptyList())
        eventDao.insertEventClassifications(event.classifications?.map { it.toEventClassificationEntity(event.id ?: throw Exception("Event ID cannot be null")) } ?: emptyList())

        // Insert each attraction and check for duplicates
        val attractionEntityList = event.embedded?.attractions?.map { it.toAttractionEntity() } ?: emptyList()
        for (attractionToSave in attractionEntityList) {
            val existingAttraction = eventDao.getAttractionById(attractionToSave.attractionId)
            if (existingAttraction == null) {
                eventDao.insertAttraction(attractionToSave) // Simple insert
            } else {
                // Merge data if needed and update
                val updatedAttraction = existingAttraction.copy(
                    name = attractionToSave.name,
                    description = attractionToSave.description,
                    url = attractionToSave.url,
                    additionalInfo = attractionToSave.additionalInfo,
                )
                eventDao.updateAttraction(updatedAttraction) // @Update method
            }
        }

        // Insert each venue and check for duplicates
        val venueEntityList = event.embedded?.venues?.map { it.toVenueEntity() } ?: emptyList()
        for (venueToSave in venueEntityList) {
            val existingVenue = eventDao.getVenueById(venueToSave.venueId) // Assuming getVenueById in a VenueDao
            if (existingVenue == null) {
                eventDao.insertVenue(venueToSave) // Simple insert
            } else {
                // Merge data if needed and update
                val updatedVenue = existingVenue.copy(
                    name = venueToSave.name,
                    description = venueToSave.description,
                    address = venueToSave.address,
                    city = venueToSave.city,
                    state = venueToSave.state,
                    parkingDetail = venueToSave.parkingDetail,
                    generalRule = venueToSave.generalRule,
                    childRule = venueToSave.childRule,
                    additionalInfo = venueToSave.additionalInfo,
                    url = venueToSave.url,
                    location = venueToSave.location,
                )
                eventDao.updateVenue(updatedVenue) // @Update method
            }
        }

        // Insert the event-venue cross references
        eventDao.insertEventVenueCrossRefs(event.embedded?.venues?.map { venue -> EventVenueCrossRef(
            event.id.toString(), venue.id.toString()
        ) } ?: emptyList())

        // Insert the event-attraction cross references
        eventDao.insertEventAttractionCrossRefs(event.embedded?.attractions?.map { attraction -> EventAttractionCrossRef(
            event.id.toString(), attraction.id.toString()
        ) } ?: emptyList())

        // Insert each venue's images
        event.embedded?.venues?.forEach { venue ->
            eventDao.insertVenueImages(venue.toVenueImageEntities())
        }
        // Insert each attraction's images and classifications
        event.embedded?.attractions?.forEach { attraction ->
            eventDao.insertAttractionClassifications(attraction.classifications?.map { it.toAttractionClassificationEntity(attraction.id ?: throw Exception("Event ID cannot be null")) } ?: emptyList())
        }
        event.embedded?.attractions?.forEach { attraction ->
            eventDao.insertAttractionImages(attraction.images?.map { it.toAttractionImageEntity(attraction.id ?: throw Exception("Event ID cannot be null")) } ?: emptyList())
        }
    }

    override suspend fun getEvents(): Flow<Resource<List<Event>>> {
        return flow {
            try {
                val events = eventDao.getAllEventsWithAllDetails().map {
                    it.toEvent(
                        venues = eventDao.getVenuesForEvent(it.event.eventId),
                        attractions = eventDao.getAttractionsForEvent(it.event.eventId)
                    )
                }
                emit(Resource.Success(events))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Unknown error occurred"))
                Log.e("AppLocalEventsRepository", "Error fetching events: ${e.message}")
            }
        }
    }

    override suspend fun deleteEventById(eventId: String) {
        eventDao.deleteEventById(eventId)
    }

    override suspend fun getEventSavedById(eventId: String): Boolean {
        val event = eventDao.getEventById(eventId)
        return event != null
    }

    override suspend fun getSavedEventsIds(): Set<String> {
        return eventDao.getAllEventIds().toSet()
    }
}
