package com.example.lineup_app.data.local.entities.event_entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.lineup_app.data.remote.event_dto.Classification
import com.example.lineup_app.data.remote.event_dto.Genre
import com.example.lineup_app.data.remote.event_dto.Segment
import com.example.lineup_app.data.remote.event_dto.SubGenre

@Entity(
    tableName = "event_classifications",
    foreignKeys = [
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = ["eventId"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class EventClassificationEntity(
    @PrimaryKey(autoGenerate = true) val classificationId: Int = 0,
    val eventId: String,
    val segmentName: String?,
    val genreName: String?,
    val subGenreName: String?
) {
    fun toClassification(): Classification {
        return Classification(
            segment = Segment(name = segmentName),
            genre = Genre(name = genreName),
            subGenre = SubGenre(name = subGenreName)
        )
    }
}

@Entity(
    tableName = "attraction_classifications",
    foreignKeys = [
        ForeignKey(
            entity = AttractionEntity::class,
            parentColumns = ["attractionId"],
            childColumns = ["attractionId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AttractionClassificationEntity(
    @PrimaryKey(autoGenerate = true) val classificationId: Int = 0,
    val attractionId: String,
    val segmentName: String?,
    val genreName: String?,
    val subGenreName: String?
) {
    fun toClassification(): Classification {
        return Classification(
            segment = Segment(name = segmentName),
            genre = Genre(name = genreName),
            subGenre = SubGenre(name = subGenreName)
        )
    }
}