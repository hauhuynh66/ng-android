package com.app.model

import androidx.room.*
import java.util.*

@Entity(indices = [Index(value = ["title"], unique = true)])
data class Note (
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "content") val content: String?,
    @ColumnInfo(name = "display_date") val displayDate: Date = Date()){
    @PrimaryKey(autoGenerate = true) var id : Int = 0
}