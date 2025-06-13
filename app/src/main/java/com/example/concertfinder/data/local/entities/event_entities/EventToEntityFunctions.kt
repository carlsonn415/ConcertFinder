package com.example.concertfinder.data.local.entities.event_entities

import com.example.concertfinder.data.model.Event
import com.example.concertfinder.data.remote.event_dto.Attraction
import com.example.concertfinder.data.remote.event_dto.Classification
import com.example.concertfinder.data.remote.event_dto.EventImage
import com.example.concertfinder.data.remote.event_dto.Venue

fun EventImage.toAttractionImageEntity(
    string: String?
): VenueImageEntity {
    return VenueImageEntity(
        venueId = string ?: "",
        url = url,
        ratio = ratio,
        height = height,
        width = width,
        fallback = fallback
    )
}

fun Classification.toAttractionClassificationEntity(
    string: String?
): AttractionClassificationEntity {
    return AttractionClassificationEntity(
        attractionId = string,
        segmentName = segment?.name,
        genreName = genre?.name,
        subGenreName = subGenre?.name,
    )
}

fun Attraction.toAttractionEntity(string: String?): AttractionEntity {
    return AttractionEntity(
        attractionId = id ?: "",
        eventId = string ?: "",
        name = name,
        url = url,
        description = description,
        additionalInfo = additionalInfo
    )
}

fun Venue.toVenueImageEntities(): List<VenueImageEntity> {
    return images?.map {
        VenueImageEntity(
            venueId = id ?: "",
            url = it.url,
            ratio = it.ratio,
            height = it.height,
            width = it.width,
            fallback = it.fallback
        )
    } ?: emptyList()
}

fun Venue.toVenueEntity(string: String?): VenueEntity {
    return VenueEntity(
        venueId = id ?: "",
        eventId = string ?: "",
        name = name,
        url = url,
        description = description,
        additionalInfo = additionalInfo,
        address = address?.line1,
        city = city?.name,
        state = state?.name,
        parkingDetail = parkingDetail,
        generalRule = generalInfo?.generalRule,
        childRule = generalInfo?.childRule,
        location = LocationDataEntity(
            lat = location?.latitude,
            lon = location?.longitude
        )
    )
}

fun Classification.toEventClassificationEntity(
    string: String?
): EventClassificationEntity {
    return EventClassificationEntity(
        eventId = string,
        segmentName = segment?.name,
        genreName = genre?.name,
        subGenreName = subGenre?.name,
    )
}

fun EventImage.toEventImageEntity(string: String?): EventImageEntity {
    return EventImageEntity(
        eventId = string,
        url = url,
        ratio = ratio,
        height = height,
        width = width,
        fallback = fallback
    )
}

fun Event.toEventEntity(): EventEntity {
    return EventEntity(
        eventId = id ?: "",
        name = name,
        url = url,
        description = description,
        additionalInfo = additionalInfo,
        info = info,
        pleaseNote = pleaseNote,
        dates = DateDataEntity(
            startData = StartDataEntity(
                localStartDate = dates?.start?.localDate,
                localStartTime = dates?.start?.localTime,
                startDateTBD = dates?.start?.dateTBD,
                startDateTBA = dates?.start?.dateTBA,
                startTimeTBA = dates?.start?.timeTBA,
                noSpecificStartTime = dates?.start?.noSpecificTime
            ),
            endData = EndDataEntity(
                localEndDate = dates?.end?.localDate,
                localEndTime = dates?.end?.localTime,
                approximate = dates?.end?.approximate,
                noSpecificEndTime = dates?.end?.noSpecificTime
            ),
            accessData = AccessDataEntity(
                startDateTime = dates?.access?.startDateTime,
                startApproximate = dates?.access?.startApproximate
            ),
            timezone = dates?.timezone,
            status = StatusDataEntity(
                code = dates?.status?.code
            ),
            spanMultipleDays = dates?.spanMultipleDays
        ),
        location = LocationDataEntity(
            lat = location?.latitude,
            lon = location?.longitude
        ),
        priceRanges = PriceRangeEntity(
            min = priceRanges?.first()?.min,
            max = priceRanges?.first()?.max,
            currency = priceRanges?.first()?.currency
        ),
        place = PlaceEntity(
            address = place?.address?.line1,
            city = place?.city?.name,
            state = place?.state?.name,
        )
    )
}
