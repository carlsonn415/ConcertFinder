package com.example.lineup_app.data.remote.classification_dto


import com.example.lineup_app.data.model.Segment
import com.google.gson.annotations.SerializedName

data class SegmentDto(
    @SerializedName("_embedded")
    val embedded: EmbeddedX?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("_links")
    val links: Links?,
    @SerializedName("name")
    val name: String?
) {
    fun toSegment(): Segment {
        return Segment(
            id = id ?: "",
            name = name ?: ""
        )
    }
}