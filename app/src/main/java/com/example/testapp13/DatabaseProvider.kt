package com.example.testapp13

import android.content.Context
import androidx.room.Room
import com.example.testapp13.AppDatabase.Companion.MIGRATION_4_5

object DatabaseInstance {
    private var instance: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "patient_profiles_database"
            )
                .addMigrations(MIGRATION_4_5, AppDatabase.MIGRATION_5_6) // Добавляем обе миграции
                .build()
        }
        return instance!!
    }
}