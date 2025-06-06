package com.example.concertfinder.data.remote.classification_dto

import com.example.concertfinder.data.model.Segment
import com.example.concertfinder.data.model.Classification
import com.google.gson.annotations.SerializedName

data class ClassificationDto(
    @SerializedName("_embedded")
    val embedded: Embedded?,
    @SerializedName("_links")
    val links: LinksXXXX?,
    @SerializedName("page")
    val page: Page?
)

fun ClassificationDto.toClassificationList(): List<Classification> {

    val classifications = mutableListOf<Classification>()

    for (classification in embedded?.classifications ?: emptyList()) {
        classifications.add(
            Classification(
                segment = classification.segmentDto?.toSegment() ?: Segment("", ""),
                genres = classification.segmentDto?.embedded?.toGenreList() ?: emptyList(),
            )
        )
    }
    return classifications
}
