package com.app.model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.repository.LocationRepository
import com.app.repository.SearchHistoryRepository
import com.app.repository.SettingRepository

@Database(entities = [Setting::class, SearchHistory::class, Location::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun locationRepository() : LocationRepository
    abstract fun settingRepository() : SettingRepository
    abstract fun searchHistoryRepository() : SearchHistoryRepository
}