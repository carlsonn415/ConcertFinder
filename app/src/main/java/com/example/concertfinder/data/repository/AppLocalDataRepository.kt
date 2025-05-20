package com.example.concertfinder.data.repository

import com.example.concertfinder.domain.repository.LocalDataRepository
import com.example.concertfinder.data.local.LocalDatabaseService


class AppLocalDataRepository(
    private val localDatabaseService: LocalDatabaseService
) : LocalDataRepository {

}