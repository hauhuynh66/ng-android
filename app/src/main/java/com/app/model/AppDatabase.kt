package com.app.model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.repository.LocationRepository
import com.app.repository.NoteRepository
import com.app.repository.SearchHistoryRepository
import com.app.repository.SettingRepository
import com.app.view.DateConverter

@Database(entities = [Note::class, Location::class, Setting::class, SearchHistory::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun noteRepository(): NoteRepository
    abstract fun locationRepository() : LocationRepository
    abstract fun settingRepository() : SettingRepository
    abstract fun searchHistoryRepository() : SearchHistoryRepository
}