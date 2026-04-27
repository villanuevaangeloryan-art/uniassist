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
    entities = [
        AnnouncementEntity::class,
        TaskEntity::class,
        CampusEntity::class,
        DepartmentEntity::class,
        StudentEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun announcementDao(): AnnouncementDao
    abstract fun taskDao(): TaskDao
    abstract fun campusDao(): CampusDao
    abstract fun studentDao(): StudentDao

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
                    // Allows adding new tables without a complex migration script
                    .fallbackToDestructiveMigration()
                    .addCallback(SeedCallback())
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }

    private class SeedCallback : RoomDatabase.Callback() {
        // onCreate only runs the VERY FIRST time the database is created
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    val announcementDao = database.announcementDao()
                    val campusDao = database.campusDao()

                    // --- 1. INSERT ANNOUNCEMENTS ---
                    announcementDao.insert(
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
                    announcementDao.insert(
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
                    announcementDao.insert(
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

                    // --- 2. INSERT DUMMY CAMPUS & DEPARTMENTS ---
                    // Insert the main campus first so foreign keys work
                    campusDao.insertCampus(
                        CampusEntity(
                            id = 1,
                            name = "Main Campus",
                            address = "University Avenue",
                            contactNumber = "+63 123 456 7890"
                        )
                    )

                    // Insert the 4 default departments attached to Campus #1
                    campusDao.insertDepartment(
                        DepartmentEntity(
                            campusId = 1,
                            departmentName = "College of Computer Studies / Information Technology",
                            description = "Mr. Alvin M. Dimaunahan | alvinccsdimauanahan@gmail.com | +639829901987"
                        )
                    )
                    campusDao.insertDepartment(
                        DepartmentEntity(
                            campusId = 1,
                            departmentName = "College of Engineering",
                            description = "Ms. Jennelyn V. Tan | tanjennelyncoe@gmail.com | +639549932855"
                        )
                    )
                    campusDao.insertDepartment(
                        DepartmentEntity(
                            campusId = 1,
                            departmentName = "College of Business Administration / Accountancy",
                            description = "Ms. Vilma R. Cacho | rcachocbaa@gmail.com | +639549932855"
                        )
                    )
                    campusDao.insertDepartment(
                        DepartmentEntity(
                            campusId = 1,
                            departmentName = "College of Education",
                            description = "Ms. George C. Bacsa | geocalmacoedue@gmail.com | +639877718241"
                        )
                    )
                }
            }
        }
    }
}