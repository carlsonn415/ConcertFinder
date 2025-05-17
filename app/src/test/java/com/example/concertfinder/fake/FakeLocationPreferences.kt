package com.example.concertfinder.fake

import com.example.concertfinder.local.LocationPreferences

class FakeLocationPreferences : LocationPreferences {
    override fun saveLocation(latitude: Double, longitude: Double) {
        TODO("Not yet implemented")
    }

    override fun getLocation(): String {
        return "40.7128,-74.0060"
    }

    override fun getAddress(): String {
        return "New York, NY"
    }

}