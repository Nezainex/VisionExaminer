package com.example.testapp13

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientProfileDao {
    @Insert
    suspend fun insert(profile: PatientProfile): Long // Изменено на Long

    @Query("SELECT * FROM patient_profiles")
    fun getAllProfiles(): Flow<List<PatientProfile>>

    @Delete
    suspend fun delete(profile: PatientProfile)

    // Методы для работы с OsdiResult
    @Insert
    suspend fun insertOsdiResult(result: OsdiResult)

    @Query("SELECT * FROM osdi_results WHERE patientProfileId = :profileId")
    fun getOsdiResultsForProfile(profileId: Int): Flow<List<OsdiResult>> // Возвращаем Flow<List<OsdiResult>>

    // Методы для работы с rabkinResult
    @Insert
    suspend fun insertRabkinResult(result: RabkinResult)

    @Query("SELECT * FROM rabkin_results WHERE patientProfileId = :profileId")
    fun getrabkinResultsForProfile(profileId: Int): Flow<List<RabkinResult>> // Возвращаем Flow<List<rabkinResult>>

    // Методы для работы с ishiharaResult
    @Insert
    suspend fun insertIshiharaResult(result: IshiharaResult)

    @Query("SELECT * FROM ishihara_results WHERE patientProfileId = :profileId")
    fun getishiharaResultsForProfile(profileId: Int): Flow<List<IshiharaResult>> // Возвращаем Flow<List<ishiharaResult>>
}




