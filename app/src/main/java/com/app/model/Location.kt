package com.app.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Location(
    @ColumnInfo(name = "name") val name : String,
    @ColumnInfo(name = "lon") val lon : Double,
    @ColumnInfo(name = "lat") val lat : Double
){
    @PrimaryKey(autoGenerate = true) var id : Long = 0
}