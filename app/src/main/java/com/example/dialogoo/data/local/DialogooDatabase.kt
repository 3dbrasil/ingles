package com.example.dialogoo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        DialogueEntity::class,
        UserStatsEntity::class,
        SrsPhraseEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class DialogooDatabase : RoomDatabase() {
    abstract fun dialogueDao(): DialogueDao
    abstract fun userStatsDao(): UserStatsDao
    abstract fun srsDao(): SrsDao

    companion object {
        @Volatile
        private var INSTANCE: DialogooDatabase? = null

        fun getInstance(context: Context): DialogooDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DialogooDatabase::class.java,
                    "dialogoo_master.db"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
