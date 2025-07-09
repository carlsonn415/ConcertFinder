package com.example.lineup_app.data.remote.classification_dto

import com.example.lineup_app.data.model.Classification
import com.example.lineup_app.data.model.Segment
import com.google.gson.annotations.SerializedName

data class ClassificationApiResponse(
    @SerializedName("_embedded")
    val embedded: Embedded?,
    @SerializedName("_links")
    val links: LinksXXXX?,
    @SerializedName("page")
    val page: Page?
)

fun ClassificationApiResponse.toClassificationList(): List<Classification> {

    val classifications = mutableListOf<Classification>()

    for (classification in embedded?.classificationDtos ?: emptyList()) {
        classifications.add(
            Classification(
                segment = classification.segmentDto?.toSegment() ?: Segment("", ""),
                genres = classification.segmentDto?.embedded?.toGenreList() ?: emptyList(),
            )
        )
    }
    return classifications
}
