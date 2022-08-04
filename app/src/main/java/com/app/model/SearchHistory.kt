package com.app.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchHistory(
    @ColumnInfo(name = "activity") val activity : String,
    @ColumnInfo(name = "text") val text : String
) {
    @PrimaryKey(autoGenerate = true) val id : Long = 0
}