package com.app.repository

import androidx.room.Dao
import androidx.room.Query
import com.app.model.Location

@Dao
interface LocationRepository {
    @Query("select * from location")
    fun getAll(): Array<Location>
}