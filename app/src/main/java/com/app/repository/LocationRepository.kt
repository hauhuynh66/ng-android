package com.app.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.app.model.Location

@Dao
interface LocationRepository {
    @Query("select * from location")
    fun getAll() : Array<Location>

    @Query("select * from location where name = :name")
    fun getByName(name : String) : Location?

    @Query("select * from location limit 1")
    fun getFirst() : Location?

    @Insert
    fun insert(location: Location) : Long

    @Query("delete from location where name=:name")
    fun delete(name : String)

    @Query("select count(*) from location")
    fun count() : Int
}