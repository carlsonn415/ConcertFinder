package com.example.concertfinder.data.remote.classification_dto


import com.google.gson.annotations.SerializedName

data class Links(
    @SerializedName("self")
    val self: Self?
)