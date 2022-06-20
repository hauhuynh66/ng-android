package com.app.repository

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.app.model.Note
import java.util.*

@Dao
interface NoteDAO {
    @Query("select * from note")
    fun getAll(): LiveData<List<Note>>

    @Query("select * from note where display_date = :date")
    fun getAllByDisplayDate(date: Date) : LiveData<List<Note>>

    @Insert
    suspend fun insert(vararg notes:Note)

    @Delete
    fun delete(note:Note)
}