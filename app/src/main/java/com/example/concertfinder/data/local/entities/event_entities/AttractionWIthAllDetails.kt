package com.example.concertfinder.data.local.entities.event_entities

import androidx.room.Embedded
import androidx.room.Relation
import com.example.concertfinder.data.remote.event_dto.Attraction

data class AttractionWIthAllDetails(
    @Embedded val attraction: AttractionEntity,

    @Relation(
        parentColumn = "attractionId",
        entityColumn = "attractionId"
    )
    val images: List<AttractionImageEntity>,

    @Relation(
        parentColumn = "attractionId",
        entityColumn = "attractionId"
    )
    val classifications: List<AttractionClassificationEntity>
) {
    fun toAttraction(): Attraction {
        return Attraction(
            id = attraction.attractionId,
            name = attraction.name,
            url = attraction.url,
            description = attraction.description,
            additionalInfo = attraction.additionalInfo,
            images = images.map { it.toAttractionImage() },
            classifications = classifications.map { it.toClassification() }
        )
    }
}
