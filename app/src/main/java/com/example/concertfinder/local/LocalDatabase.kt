package com.example.concertfinder.local

// TODO: use room database to store local data
interface LocalDatabase {
    suspend fun geocodeZipCode(zipCode: String): Pair<Double, Double>
}