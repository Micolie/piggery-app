package com.piggery.app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.piggery.app.data.dao.PigDao
import com.piggery.app.data.entity.Pig

@Database(
    entities = [Pig::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PiggeryDatabase : RoomDatabase() {

    abstract fun pigDao(): PigDao

    companion object {
        @Volatile
        private var INSTANCE: PiggeryDatabase? = null

        fun getDatabase(context: Context): PiggeryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PiggeryDatabase::class.java,
                    "piggery_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
