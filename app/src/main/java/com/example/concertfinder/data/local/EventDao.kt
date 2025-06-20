package com.example.concertfinder.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.concertfinder.data.local.entities.event_entities.AttractionClassificationEntity
import com.example.concertfinder.data.local.entities.event_entities.AttractionEntity
import com.example.concertfinder.data.local.entities.event_entities.AttractionImageEntity
import com.example.concertfinder.data.local.entities.event_entities.EventAttractionCrossRef
import com.example.concertfinder.data.local.entities.event_entities.pojos.AttractionWIthAllDetails
import com.example.concertfinder.data.local.entities.event_entities.EventClassificationEntity
import com.example.concertfinder.data.local.entities.event_entities.EventEntity
import com.example.concertfinder.data.local.entities.event_entities.EventImageEntity
import com.example.concertfinder.data.local.entities.event_entities.EventVenueCrossRef
import com.example.concertfinder.data.local.entities.event_entities.pojos.EventWithAllDetails
import com.example.concertfinder.data.local.entities.event_entities.VenueEntity
import com.example.concertfinder.data.local.entities.event_entities.VenueImageEntity
import com.example.concertfinder.data.local.entities.event_entities.pojos.VenueWithAllDetails

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

    // --- Junction Table Insert ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEventVenueCrossRefs(crossRefs: List<EventVenueCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEventVenueCrossRef(crossRef: EventVenueCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEventAttractionCrossRefs(crossRefs: List<EventAttractionCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEventAttractionCrossRef(crossRef: EventAttractionCrossRef)

    // --- Update ---
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateVenue(venue: VenueEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAttraction(attraction: AttractionEntity)

    // --- Getters ---
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
    @Query("""
    SELECT v.*
    FROM venues v
    INNER JOIN event_venue_cross_ref evc ON v.venueId = evc.venueId
    WHERE evc.eventId = :eventId
""")
    suspend fun getVenuesForEvent(eventId: String): List<VenueWithAllDetails>

    @Transaction
    @Query("""
    SELECT a.*
    FROM attractions a
    INNER JOIN event_attraction_cross_ref evc ON a.attractionId = evc.attractionId
    WHERE evc.eventId = :eventId
    """)
    suspend fun getAttractionsForEvent(eventId: String): List<AttractionWIthAllDetails>

    @Query("SELECT * FROM events WHERE eventId = :eventId")
    suspend fun getEventById(eventId: String): EventEntity?

    @Query("SELECT * FROM venues WHERE venueId = :venueId")
    suspend fun getVenueById(venueId: String): VenueEntity?

    @Query("SELECT * FROM attractions WHERE attractionId = :attractionId")
    suspend fun getAttractionById(attractionId: String): AttractionEntity?

}