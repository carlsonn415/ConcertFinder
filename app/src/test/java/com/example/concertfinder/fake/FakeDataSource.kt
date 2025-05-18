package com.example.concertfinder.fake

import com.example.concertfinder.model.apidata.Event
import com.example.concertfinder.model.apidata.EventImage

object FakeDataSource {

    val eventsList = listOf(
        Event(
            id = "1",
            name = "Test Event 1",
            images = listOf(
                EventImage(
                    url = "https://example.com/image1.jpg"
                ),
                EventImage(
                    url = "https://example.com/image2.jpg"
                )
            ),
        ),
        Event(
            id = "2",
            name = "Test Event 2",
            images = listOf(
                EventImage(
                    url = "https://example.com/image1.jpg"
                ),
                EventImage(
                    url = "https://example.com/image2.jpg"
                )
            ),
        )
    )
}