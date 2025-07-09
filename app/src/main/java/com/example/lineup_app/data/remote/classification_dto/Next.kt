package com.example.lineup_app.data.remote.classification_dto


import com.google.gson.annotations.SerializedName

data class Next(
    @SerializedName("href")
    val href: String?,
    @SerializedName("templated")
    val templated: Boolean?
)