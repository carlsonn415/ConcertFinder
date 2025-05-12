package com.example.concertfinder

import android.app.Application
import com.example.concertfinder.data.AppContainer
import com.example.concertfinder.data.DefaultAppContainer

class ConcertFinderApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}