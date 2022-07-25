package com.app.model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.repository.LocationRepository
import com.app.repository.NoteRepository
import com.app.view.DateConverter

@Database(entities = [Note::class, Location::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun noteRepository(): NoteRepository
    abstract fun locationRepository() : LocationRepository
}