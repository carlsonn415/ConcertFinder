package com.example.concertfinder.data.remote.classification_dto


import com.google.gson.annotations.SerializedName

data class Embedded(
    @SerializedName("classifications")
    val classificationDtos: List<ClassificationDto>?
)