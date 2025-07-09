package com.example.lineup_app.data.local.entities.event_entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "venues",
    indices = [Index("venueId")]
)
data class VenueEntity(
    @PrimaryKey val venueId: String,
    val name: String?,
    val description: String?,
    val url: String?,
    val address: String?,
    val city: String?,
    val state: String?,
    val parkingDetail: String?,
    val generalRule: String?,
    val additionalInfo: String?,
    val childRule: String?,
    @Embedded val location: LocationDataEntity?,
)