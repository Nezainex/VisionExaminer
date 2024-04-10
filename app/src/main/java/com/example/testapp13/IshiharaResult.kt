package com.example.testapp13

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ishihara_results")
data class IshiharaResult(
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

    companion object CREATOR : Parcelable.Creator<IshiharaResult> {
        override fun createFromParcel(parcel: Parcel): IshiharaResult {
            return IshiharaResult(parcel)
        }

        override fun newArray(size: Int): Array<IshiharaResult?> {
            return arrayOfNulls(size)
        }
    }
}