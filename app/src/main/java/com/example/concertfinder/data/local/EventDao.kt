package com.example.concertfinder.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.concertfinder.data.local.entities.event_entities.AttractionClassificationEntity
import com.example.concertfinder.data.local.entities.event_entities.AttractionEntity
import com.example.concertfinder.data.local.entities.event_entities.AttractionImageEntity
import com.example.concertfinder.data.local.entities.event_entities.AttractionWIthAllDetails
import com.example.concertfinder.data.local.entities.event_entities.EventClassificationEntity
import com.example.concertfinder.data.local.entities.event_entities.EventEntity
import com.example.concertfinder.data.local.entities.event_entities.EventImageEntity
import com.example.concertfinder.data.local.entities.event_entities.EventWithAllDetails
import com.example.concertfinder.data.local.entities.event_entities.VenueEntity
import com.example.concertfinder.data.local.entities.event_entities.VenueImageEntity
import com.example.concertfinder.data.local.entities.event_entities.VenueWithAllDetails

@Dao
interface EventDao {

    // --- Basic Inserts for individual entities (handle conflicts appropriately) ---
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertEvent(event: EventEntity): Long

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertEventImages(images: List<EventImageEntity>)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAttraction(attraction: AttractionEntity)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAttractions(attractions: List<AttractionEntity>)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertVenue(venue: VenueEntity)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertVenues(venues: List<VenueEntity>)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertEventClassification(classification: EventClassificationEntity)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertEventClassifications(classifications: List<EventClassificationEntity>)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAttractionClassification(classification: AttractionClassificationEntity)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAttractionClassifications(classifications: List<AttractionClassificationEntity>)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertVenueImage(image: VenueImageEntity)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertVenueImages(images: List<VenueImageEntity>)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAttractionImage(image: AttractionImageEntity)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAttractionImages(images: List<AttractionImageEntity>)

    @Transaction
    @Query("SELECT * FROM events WHERE eventId = :eventId")
    suspend fun getEventWithAllDetails(eventId: String): EventWithAllDetails

    @Transaction
    @Query("SELECT * FROM events")
    suspend fun getAllEventsWithAllDetails(): List<EventWithAllDetails>

    @Transaction
    @Query("DELETE FROM events WHERE eventId = :eventId")
    suspend fun deleteEventById(eventId: String)

    @Transaction
    @Query("SELECT * FROM venues WHERE eventId = :eventId")
    suspend fun getVenuesForEvent(eventId: String): List<VenueWithAllDetails>

    @Transaction
    @Query("SELECT * FROM attractions WHERE eventId = :eventId")
    suspend fun getAttractionsForEvent(eventId: String): List<AttractionWIthAllDetails>
}