package com.example.lineup_app.data.remote.classification_dto


import com.google.gson.annotations.SerializedName

data class SubgenreDto(
    @SerializedName("id")
    val id: String?,
    @SerializedName("_links")
    val links: Links?,
    @SerializedName("name")
    val name: String?
)