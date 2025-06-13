package com.example.concertfinder.data.remote.event_dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class DateData(
    @SerializedName("start") val start: StartData? = null, // start date and time
    @SerializedName("end") val end: EndData? = null, // end date and time
    @SerializedName("access") val access: AccessData? = null, // access date and time
    @SerializedName("timezone") val timezone: String? = null, // timezone of event
    @SerializedName("status") val status: StatusData? = null, // status of event
    @SerializedName("spanMultipleDays") val spanMultipleDays: Boolean? = null // event spans multiple days
)

@Serializable
data class StartData(
    @SerializedName("localDate") val localDate: String? = null, // start date
    @SerializedName("localTime") val localTime: String? = null, // start time
    @SerializedName("dateTBD") val dateTBD: Boolean? = null, // start date is to be decided
    @SerializedName("startDateTBA") val dateTBA: Boolean? = null, // start date is to be announced
    @SerializedName("timeTBA") val timeTBA: Boolean? = null, // start time is to be announced
    @SerializedName("noSpecificTime") val noSpecificTime: Boolean? = null // start date is not specific
)

@Serializable
data class EndData(
    @SerializedName("localDate") val localDate: String? = null, // end date
    @SerializedName("localTime") val localTime: String? = null, // end time
    @SerializedName("approximate") val approximate: Boolean? = null, // end date is approximated
    @SerializedName("noSpecificTime") val noSpecificTime: Boolean? = null // end date is not specific
)

@Serializable
data class AccessData(
    @SerializedName("startDateTime") val startDateTime: String? = null, // access start date and time
    @SerializedName("startApproximate") val startApproximate: Boolean? = null, // access start date and time is approximated
)

@Serializable
data class StatusData(
    @SerializedName("code") val code: String? = null, // status code
)