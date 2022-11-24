package com.app.data.score

import android.os.Parcel
import android.os.Parcelable

data class FootballTeam(val id: Int, val name: String?, val iconUrl: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FootballTeam

        if (id != other.id) return false
        if (name != other.name) return false
        if (iconUrl != other.iconUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + iconUrl.hashCode()
        return result
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(par: Parcel, flag: Int) {
        par.writeInt(id)
        par.writeString(name)
        par.writeString(iconUrl)
    }

    companion object CREATOR : Parcelable.Creator<FootballTeam> {
        override fun createFromParcel(parcel: Parcel): FootballTeam {
            return FootballTeam(parcel)
        }

        override fun newArray(size: Int): Array<FootballTeam?> {
            return arrayOfNulls(size)
        }
    }
}