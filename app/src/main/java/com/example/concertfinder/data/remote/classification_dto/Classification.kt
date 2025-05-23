package com.example.concertfinder.data.remote.classification_dto


import com.google.gson.annotations.SerializedName

data class Classification(
    @SerializedName("_links")
    val links: Links?,
    @SerializedName("segment")
    val segmentDto: SegmentDto?
)