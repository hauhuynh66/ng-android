package com.app.repository

import androidx.room.*
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

    @Transaction
    fun checkInsert(location: Location) : Int {
        val name = getByName(location.name)
        return if(name == null){
            insert(location)
            1
        }else{
            0
        }
    }

    @Query("delete from location where name=:name")
    fun delete(name : String)

    @Query("select count(*) from location")
    fun count() : Int

    @Query("select name from location where lat = :lat and lon = :lon")
    fun getByCoordinate(lat : Double, lon : Double) : String?

    @Query("update location set name = :newName where name = :oldName")
    fun updateName(newName : String, oldName : String)
}