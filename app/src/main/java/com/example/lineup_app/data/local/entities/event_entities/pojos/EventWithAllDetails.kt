package com.example.lineup_app.data.local.entities.event_entities.pojos

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.lineup_app.data.local.entities.event_entities.AttractionEntity
import com.example.lineup_app.data.local.entities.event_entities.EventAttractionCrossRef
import com.example.lineup_app.data.local.entities.event_entities.EventClassificationEntity
import com.example.lineup_app.data.local.entities.event_entities.EventEntity
import com.example.lineup_app.data.local.entities.event_entities.EventImageEntity
import com.example.lineup_app.data.local.entities.event_entities.EventVenueCrossRef
import com.example.lineup_app.data.local.entities.event_entities.VenueEntity
import com.example.lineup_app.data.model.Event
import com.example.lineup_app.data.remote.event_dto.AccessData
import com.example.lineup_app.data.remote.event_dto.Address
import com.example.lineup_app.data.remote.event_dto.City
import com.example.lineup_app.data.remote.event_dto.DateData
import com.example.lineup_app.data.remote.event_dto.EmbeddedEventData
import com.example.lineup_app.data.remote.event_dto.EndData
import com.example.lineup_app.data.remote.event_dto.LocationData
import com.example.lineup_app.data.remote.event_dto.Place
import com.example.lineup_app.data.remote.event_dto.PriceRange
import com.example.lineup_app.data.remote.event_dto.StartData
import com.example.lineup_app.data.remote.event_dto.State
import com.example.lineup_app.data.remote.event_dto.StatusData

data class EventWithAllDetails(
    @Embedded val event: EventEntity,

    @Relation(
        parentColumn = "eventId",
        entityColumn = "eventId"
    )
    val images: List<EventImageEntity>,

    @Relation(
        parentColumn = "eventId",
        entity = VenueEntity::class,
        entityColumn = "venueId",
        associateBy = Junction(EventVenueCrossRef::class)
    )
    val venues: List<VenueEntity>,

    @Relation(
        parentColumn = "eventId",
        entity = AttractionEntity::class,
        entityColumn = "attractionId",
        associateBy = Junction(EventAttractionCrossRef::class)
    )
    val attractions: List<AttractionEntity>,

    @Relation(
        parentColumn = "eventId",
        entityColumn = "eventId"
    )
    val classifications: List<EventClassificationEntity>

) {
    fun toEvent(
        venues: List<VenueWithAllDetails>,
        attractions: List<AttractionWIthAllDetails>
    ): Event {
        return Event(
            embedded = EmbeddedEventData(
                venues = venues.map { it.toVenue() },
                attractions = attractions.map { it.toAttraction() }
            ),
            id = event.eventId,
            name = event.name,
            location = LocationData(
                latitude = event.location?.lat,
                longitude = event.location?.lon
            ),
            url = event.url,
            description = event.description,
            additionalInfo = event.additionalInfo,
            dates = DateData(
                start = StartData(
                    localDate = event.dates?.startData?.localStartDate,
                    localTime = event.dates?.startData?.localStartTime,
                    dateTBD = event.dates?.startData?.startDateTBD,
                    dateTBA = event.dates?.startData?.startDateTBA,
                    timeTBA = event.dates?.startData?.startTimeTBA,
                    noSpecificTime = event.dates?.startData?.noSpecificStartTime
                ),
                end = EndData(
                    localDate = event.dates?.endData?.localEndDate,
                    localTime = event.dates?.endData?.localEndTime,
                    approximate = event.dates?.endData?.approximate,
                    noSpecificTime = event.dates?.endData?.noSpecificEndTime
                ),
                access = AccessData(
                    startDateTime = event.dates?.accessData?.startDateTime,
                    startApproximate = event.dates?.accessData?.startApproximate
                ),
                timezone = event.dates?.timezone,
                status = StatusData(code = event.dates?.status?.code),
                spanMultipleDays = event.dates?.spanMultipleDays
            ),
            images = images.map { it.toEventImage() },
            info = event.info,
            pleaseNote = event.pleaseNote,
            priceRanges = listOf(event.priceRanges?.toPriceRange() ?: PriceRange()),
            classifications = classifications.map { it.toClassification() },
            place = Place(
                address = Address(event.place?.address),
                city = City(event.place?.city),
                state = State(event.place?.state)
            ),
            saved = true
        )
    }
}