package com.example.testapp13

import android.content.Context
import androidx.room.Room

object DatabaseInstance {

    private var instance: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "patient_profiles_database"
            ).addMigrations(AppDatabase.MIGRATION_4_5) // Добавляем миграцию
                .build()
        }
        return instance!!
    }
}