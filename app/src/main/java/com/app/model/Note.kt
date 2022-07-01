package com.app.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Note (
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "content") val content: String?,
    @ColumnInfo(name = "display_date") val displayDate: Date = Date()){
    @PrimaryKey(autoGenerate = true) var id : Int = 0
}