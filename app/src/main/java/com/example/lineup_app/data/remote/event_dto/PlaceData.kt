package com.example.lineup_app.data.remote.event_dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Place(
    @SerializedName("address") val address: Address? = null,
    @SerializedName("city") val city: City? = null,
    @SerializedName("state") val state: State? = null,
)

@Serializable
data class Address(
    @SerializedName("line1") val line1: String? = null
)

@Serializable
data class City(
    @SerializedName("name") val name: String? = null
)

@Serializable
data class State(
    @SerializedName("name") val name: String? = null
)