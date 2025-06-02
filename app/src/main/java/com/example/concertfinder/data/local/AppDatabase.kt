package com.example.concertfinder.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.concertfinder.data.local.entities.GenreEntity
import com.example.concertfinder.data.local.entities.SegmentEntity
import com.example.concertfinder.data.local.entities.SubgenreEntity


@Database(
    entities = [
        SegmentEntity::class,
        GenreEntity::class,
        SubgenreEntity::class
    ],
    version = 1 // Increment this version when you make changes to the database
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun classificationDao(): ClassificationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context = context.applicationContext,
                    klass = AppDatabase::class.java,
                    name = "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}