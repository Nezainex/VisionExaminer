package com.example.testapp13

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [PatientProfile::class, OsdiResult::class, RabkinResult::class],
    version = 5
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun patientProfileDao(): PatientProfileDao

    companion object {
        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS `rabkin_results` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `patientProfileId` INTEGER NOT NULL, `score` INTEGER NOT NULL, `resultText` TEXT NOT NULL)")
            }
        }
    }
}