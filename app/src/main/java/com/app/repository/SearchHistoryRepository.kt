package com.app.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.app.model.SearchHistory

@Dao
interface SearchHistoryRepository {
    @Query("select text from searchhistory where activity = :a")
    fun getAllByActivity(a : String) : List<String>

    @Insert
    fun insert(history: SearchHistory)
}