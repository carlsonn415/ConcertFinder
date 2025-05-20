package com.example.concertfinder.fake

import android.location.Location
import com.example.concertfinder.data.remote.LocationManagerService

class FakeLocationManagerService : LocationManagerService {
    override suspend fun getLocation(): Location? {

        return Location("fake_provider").apply {
            latitude = 40.7128
            longitude = -74.0060
        }
    }
}