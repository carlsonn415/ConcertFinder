package com.example.concertfinder.model.apidata

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Venue(
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("url") val url: String? = null,
    @SerializedName("address") val address: Address? = null,
    @SerializedName("city") val city: City? = null,
    @SerializedName("state") val state: State? = null,
    @SerializedName("images") val images: List<EventImage>? = null,
    @SerializedName("parkingDetail") val parkingDetail: String? = null,
    @SerializedName("generalInfo") val generalInfo: GeneralInfo? = null,
    @SerializedName("additionalInfo") val additionalInfo: String? = null,
)

@Serializable
data class GeneralInfo(
    @SerializedName("generalRule") val generalRule: String? = null,
    @SerializedName("childRule") val childRule: String? = null,
)