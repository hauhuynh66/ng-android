package com.app.model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.repository.NoteDAO
import com.app.view.DateConverter

@Database(entities = [Note::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun noteDAO(): NoteDAO
}