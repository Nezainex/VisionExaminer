package com.example.testapp13

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [PatientProfile::class, OsdiResult::class, RabkinResult::class, IshiharaResult::class],
    version = 6
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun patientProfileDao(): PatientProfileDao

    companion object {
        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Здесь добавьте SQL-запросы для изменения схемы с версии 4 на версию 5
                // Например, если вы добавили таблицу "ishihara_results" в версии 5:
                db.execSQL("CREATE TABLE IF NOT EXISTS `rabkin_results` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `patientProfileId` INTEGER NOT NULL, `score` INTEGER NOT NULL, `resultText` TEXT NOT NULL)")
            }
        }

        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS `ishihara_results` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `patientProfileId` INTEGER NOT NULL, `score` INTEGER NOT NULL, `resultText` TEXT NOT NULL)")
            }
        }
    }
}