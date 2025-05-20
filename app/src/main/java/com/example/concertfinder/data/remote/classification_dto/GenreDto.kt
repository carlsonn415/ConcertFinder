package com.example.concertfinder.data.remote.classification_dto


import com.google.gson.annotations.SerializedName

data class GenreDto(
    @SerializedName("_embedded")
    val embedded: EmbeddedXX?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("_links")
    val links: Links?,
    @SerializedName("name")
    val name: String?
)