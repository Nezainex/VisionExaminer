package com.example.testapp13

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [PatientProfile::class, OsdiResult::class, RabkinResult::class, IshiharaResult::class],
    version = 8
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun patientProfileDao(): PatientProfileDao

    companion object {
        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS `rabkin_results` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `patientProfileId` INTEGER NOT NULL, `score` INTEGER NOT NULL, `resultText` TEXT NOT NULL)")
            }
        }

        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS `ishihara_results` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `patientProfileId` INTEGER NOT NULL, `score` INTEGER NOT NULL, `resultText` TEXT NOT NULL)")
            }
        }
        val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE patient_profiles ADD COLUMN birthDate TEXT NOT NULL DEFAULT ''")
            }
        }

        val MIGRATION_4_7 = object : Migration(4, 7) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Migration steps from 4 to 5
                db.execSQL("CREATE TABLE IF NOT EXISTS `rabkin_results` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `patientProfileId` INTEGER NOT NULL, `score` INTEGER NOT NULL, `resultText` TEXT NOT NULL)")

                // Migration steps from 5 to 6
                db.execSQL("CREATE TABLE IF NOT EXISTS `ishihara_results` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `patientProfileId` INTEGER NOT NULL, `score` INTEGER NOT NULL, `resultText` TEXT NOT NULL)")

                // Migration steps from 6 to 7
                db.execSQL("ALTER TABLE patient_profiles ADD COLUMN birthDate TEXT NOT NULL DEFAULT ''")
            }
        }
        val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE patient_profiles RENAME COLUMN date TO examinationdate")
            }
        }
    }
}