package com.example.visionExaminer.data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patient_profiles")
data class PatientProfile(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val birthDate: String = "",
    val lastName: String,
    val firstName: String,
    val middleName: String,
    val gender: String,
    val age: Double,
    val examinationdate: String,
    val visOD: Double,
    val visOS: Double,
    val visOU: Double,
    val visODcorr: Double,
    val visOScorr: Double,
    val visOUcorr: Double,
    val sphOD: Double,
    val cylOD: Double,
    val axOD: Double,
    val sphOS: Double,
    val cylOS: Double,
    val axOS: Double,
    val sphODLabel: String,
    val cylODLabel: String,
    val axODLabel: String,
    val sphOSLabel: String,
    val cylOSLabel: String,
    val axOSLabel: String,
    val comparesphResult: String,
    val midriaticAgent: String,
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(), // Read the id value
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(birthDate)
        parcel.writeString(lastName)
        parcel.writeString(firstName)
        parcel.writeString(middleName)
        parcel.writeString(gender)
        parcel.writeDouble(age)
        parcel.writeString(examinationdate)
        parcel.writeDouble(visOD)
        parcel.writeDouble(visOS)
        parcel.writeDouble(visOU)
        parcel.writeDouble(visODcorr)
        parcel.writeDouble(visOScorr)
        parcel.writeDouble(visOUcorr)
        parcel.writeDouble(sphOD)
        parcel.writeDouble(cylOD)
        parcel.writeDouble(axOD)
        parcel.writeDouble(sphOS)
        parcel.writeDouble(cylOS)
        parcel.writeDouble(axOS)
        parcel.writeString(sphODLabel)
        parcel.writeString(cylODLabel)
        parcel.writeString(axODLabel)
        parcel.writeString(sphOSLabel)
        parcel.writeString(cylOSLabel)
        parcel.writeString(axOSLabel)
        parcel.writeString(comparesphResult)
        parcel.writeString(midriaticAgent)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PatientProfile> {
        override fun createFromParcel(parcel: Parcel): PatientProfile {
            return PatientProfile(parcel)
        }

        override fun newArray(size: Int): Array<PatientProfile?> {
            return arrayOfNulls(size)
        }
    }
}