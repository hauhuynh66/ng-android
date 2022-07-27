package com.app.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.app.model.Setting

@Dao
interface SettingRepository {
    @Query("select value from setting where name = :name")
    fun getProperty(name : String) : String?

    @Insert
    fun insert(setting : Setting) : Long

    @Query("update setting set value = :value where name = :name")
    fun update(name : String, value : String)
}