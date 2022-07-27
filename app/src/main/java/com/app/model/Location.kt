package com.app.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Location(
    @PrimaryKey val name : String,
    @ColumnInfo(name = "lon") val lon : Double,
    @ColumnInfo(name = "lat") val lat : Double
)