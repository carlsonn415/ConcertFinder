package com.example.concertfinder.data.repositories

import com.example.concertfinder.local.LocalDatabase

interface LocalDataRepository {
    suspend fun geocodeZipCode(zipCode: String): Pair<Double, Double>
}

class OnDeviceLocalDataRepository(
    private val localDatabase: LocalDatabase
) : LocalDataRepository {
    override suspend fun geocodeZipCode(zipCode: String): Pair<Double, Double> =
        localDatabase.geocodeZipCode(zipCode)
}