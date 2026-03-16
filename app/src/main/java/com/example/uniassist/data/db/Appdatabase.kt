package com.example.uniassist.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(
    entities = [AnnouncementEntity::class, TaskEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun announcementDao(): AnnouncementDao
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "uniassist_db"
                )
                    .addCallback(SeedCallback())
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }

    private class SeedCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Run inserts off the main thread
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    val dao = database.announcementDao()
                    dao.insert(
                        AnnouncementEntity(
                            title      = "Campus Operations & Class Suspension",
                            date       = "January 19, 2026",
                            content    = "All on-campus classes are suspended due to bad weather.",
                            bulletsRaw = "Online Classes:|Asynchronous via portal||Offices:|Skeletal workforce||Campus Access:|Essential staff only",
                            footer     = "Stay safe everyone!",
                            category   = "Emergency",
                            isPriority = true,
                            createdAt  = 1L
                        )
                    )
                    dao.insert(
                        AnnouncementEntity(
                            title      = "Scholarship Applications Open",
                            date       = "January 25, 2026",
                            content    = "Students with GPA 1.75+ may apply.",
                            bulletsRaw = "Deadline:|Feb 10||Where:|Student Portal||Contact:|Scholarship Office",
                            footer     = "Don't miss this opportunity!",
                            category   = "Academic",
                            isPriority = false,
                            createdAt  = 2L
                        )
                    )
                    dao.insert(
                        AnnouncementEntity(
                            title      = "Library Hours Extended",
                            date       = "February 1, 2026",
                            content    = "The university library will be open until 10 PM during exam week.",
                            bulletsRaw = "New Hours:|8 AM – 10 PM||Quiet Zones:|Available on 3rd floor||Book Reservations:|Online portal",
                            footer     = "Good luck with your exams!",
                            category   = "General",
                            isPriority = false,
                            createdAt  = 3L
                        )
                    )
                }
            }
        }
    }
}