package com.example.VisionExaminer

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "midriatic_results")
data class MidriaticResult(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val patientProfileId: Int,
    val midriaticAgent: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(), // Считываем id
        parcel.readInt(), //  !!! Считываем patientProfileId !!!
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(patientProfileId) // Записываем patientProfileId
        parcel.writeString(midriaticAgent)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MidriaticResult> {
        override fun createFromParcel(parcel: Parcel): MidriaticResult {
            return MidriaticResult(parcel)
        }

        override fun newArray(size: Int): Array<MidriaticResult?> {
            return arrayOfNulls(size)
        }
    }
}