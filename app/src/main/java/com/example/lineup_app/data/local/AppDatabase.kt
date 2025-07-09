package com.example.lineup_app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lineup_app.data.local.entities.classification_entities.GenreEntity
import com.example.lineup_app.data.local.entities.classification_entities.SegmentEntity
import com.example.lineup_app.data.local.entities.classification_entities.SubgenreEntity
import com.example.lineup_app.data.local.entities.event_entities.AttractionClassificationEntity
import com.example.lineup_app.data.local.entities.event_entities.AttractionEntity
import com.example.lineup_app.data.local.entities.event_entities.AttractionImageEntity
import com.example.lineup_app.data.local.entities.event_entities.EventAttractionCrossRef
import com.example.lineup_app.data.local.entities.event_entities.EventClassificationEntity
import com.example.lineup_app.data.local.entities.event_entities.EventEntity
import com.example.lineup_app.data.local.entities.event_entities.EventImageEntity
import com.example.lineup_app.data.local.entities.event_entities.EventVenueCrossRef
import com.example.lineup_app.data.local.entities.event_entities.VenueEntity
import com.example.lineup_app.data.local.entities.event_entities.VenueImageEntity


@Database(
    entities = [
        SegmentEntity::class,
        GenreEntity::class,
        SubgenreEntity::class,
        AttractionEntity::class,
        EventEntity::class,
        AttractionImageEntity::class,
        EventImageEntity::class,
        VenueImageEntity::class,
        VenueEntity::class,
        AttractionClassificationEntity::class,
        EventClassificationEntity::class,
        EventVenueCrossRef::class,
        EventAttractionCrossRef::class,
    ],
    version = 5 // Increment this version when you make changes to the database
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun classificationDao(): ClassificationDao

    abstract fun eventDao(): EventDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context = context.applicationContext,
                    klass = AppDatabase::class.java,
                    name = "app_database"
                )
                //.fallbackToDestructiveMigration(true)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}