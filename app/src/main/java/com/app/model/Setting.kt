package com.app.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Setting(
    @PrimaryKey val name : String,
    @ColumnInfo(name = "value") val value : String
)