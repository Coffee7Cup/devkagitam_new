package com.yash.devkagitam.db.api

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ApiDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(api : ApiEntity)

    @Update
    suspend fun update(api : ApiEntity)

    @Delete
    suspend fun delete(api: ApiEntity)

    @Query("Select * from api_db")
    suspend fun getAllApis() : List<ApiEntity>

    @Query("SELECT * FROM api_db WHERE owner = :owner")
    suspend fun getApiByOwner(owner : String) : ApiEntity
}