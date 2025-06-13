package com.example.concertfinder.data.local.entities.event_entities

import androidx.room.Embedded

data class DateDataEntity(
    @Embedded val startData: StartDataEntity?,
    @Embedded val endData: EndDataEntity?,
    @Embedded val accessData: AccessDataEntity?,
    val timezone: String?,
    @Embedded val status: StatusDataEntity?,
    val spanMultipleDays: Boolean?
)

data class StartDataEntity(
    val localStartDate: String?,
    val localStartTime: String?,
    val startDateTBD: Boolean?,
    val startDateTBA: Boolean?,
    val startTimeTBA: Boolean?,
    val noSpecificStartTime: Boolean?
)

data class EndDataEntity(
    val localEndDate: String?,
    val localEndTime: String?,
    val approximate: Boolean?,
    val noSpecificEndTime: Boolean?
)

data class AccessDataEntity(
    val startDateTime: String?,
    val startApproximate: Boolean?
)

data class StatusDataEntity(
    val code: String?
)
