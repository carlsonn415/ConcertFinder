package com.example.concertfinder.fake

import com.example.concertfinder.model.DateInfo
import com.example.concertfinder.model.Event
import com.example.concertfinder.model.EventImage
import com.example.concertfinder.model.StartDateInfo

object FakeDataSource {

    val eventsList = listOf(
        Event(
            id = "1",
            name = "Test Event 1",
            location = Pair(1.0, 2.0),
            imageList = listOf(
                EventImage(
                    url = "https://example.com/image1.jpg"
                ),
                EventImage(
                    url = "https://example.com/image2.jpg"
                )
            ),
            dateInfo = DateInfo(
                start = StartDateInfo(
                    date = "2023-09-15",
                    time = "19:30:00"
                )
            )
        ),
        Event(
            id = "2",
            name = "Test Event 2",
            location = Pair(1.0, 2.0),
            imageList = listOf(
                EventImage(
                    url = "https://example.com/image1.jpg"
                ),
                EventImage(
                    url = "https://example.com/image2.jpg"
                )
            ),
            dateInfo = DateInfo(
                start = StartDateInfo(
                    date = "2023-09-15",
                    time = "19:30:00"
                )
            )
        )
    )
}