package com.app.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.app.model.Note
import java.util.*

@Dao
interface NoteDAO {
    @Query("select * from note")
    fun getAll(): Array<Note>

    @Query("select * from note where display_date = :date")
    fun getAllByDisplayDate(date: Date) : Array<Note>

    @Insert
    suspend fun insert(vararg notes:Note)

    @Delete
    fun delete(note:Note)

    @Query("delete from note")
    fun clear()
}