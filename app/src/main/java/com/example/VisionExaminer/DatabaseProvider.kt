package com.example.VisionExaminer

import android.content.Context
import androidx.room.Room

object DatabaseInstance {
    @Volatile
    private var instance: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
        return instance ?: synchronized(this) {
            val newInstance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "patient_profiles_database"
            )
                .fallbackToDestructiveMigration() // Используем destructive migration
                .build()
            instance = newInstance
            newInstance
        }
    }
}
