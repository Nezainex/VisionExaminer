package com.example.VisionExaminer

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rabkin_results")
data class RabkinResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var patientProfileId: Int, // Связь с профилем пациента
    val score: Int,
    val resultText: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(patientProfileId)
        parcel.writeInt(score)
        parcel.writeString(resultText)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RabkinResult> {
        override fun createFromParcel(parcel: Parcel): RabkinResult {
            return RabkinResult(parcel)
        }

        override fun newArray(size: Int): Array<RabkinResult?> {
            return arrayOfNulls(size)
        }
    }
}