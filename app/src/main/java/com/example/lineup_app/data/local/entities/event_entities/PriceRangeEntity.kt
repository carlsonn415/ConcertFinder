package com.example.lineup_app.data.local.entities.event_entities

import com.example.lineup_app.data.remote.event_dto.PriceRange

data class PriceRangeEntity(
    val min: Double?,
    val max: Double?,
    val currency: String?
) {
    fun toPriceRange(): PriceRange {
        return PriceRange(
            min = min,
            max = max,
            currency = currency
        )
    }
}