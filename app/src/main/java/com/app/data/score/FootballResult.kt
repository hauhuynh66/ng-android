package com.app.data.score

import android.os.Parcel
import android.os.Parcelable

class FootballResult(val homeTeam : FootballTeam,
                     val awayTeam: FootballTeam,
                     val referee : String,
                     val homeGoal : Int?,
                     val awayGoal : Int?,
                     val matchId : Long) : Parcelable{
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(par: Parcel, flag: Int) {
        par.writeParcelable(homeTeam, flag)
        par.writeParcelable(awayTeam, flag)
        par.writeString(referee)
        par.writeString(homeGoal.toString())
        par.writeString(awayGoal.toString())
        par.writeLong(matchId)
    }

    companion object CREATOR : Parcelable.Creator<FootballResult> {
        override fun createFromParcel(parcel: Parcel): FootballResult {
            return FootballResult(
                parcel.readParcelable(FootballTeam::class.java.classLoader)!!,
                parcel.readParcelable(FootballTeam::class.java.classLoader)!!,
                parcel.readString()!!,
                parcel.readString()?.toInt(),
                parcel.readString()?.toInt(),
                parcel.readLong()
            )
        }

        override fun newArray(size: Int): Array<FootballResult?> {
            return arrayOfNulls(size)
        }
    }
}