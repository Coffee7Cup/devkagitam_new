package com.yash.kagitam.db.colorSchemes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ColorSchemeDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(scheme : ColorSchemeEntity)

    @Delete
    fun delete(scheme : ColorSchemeEntity)

    @Query("DELETE FROM color_schemes WHERE id = :schemeId")
    fun deleteById(schemeId : Int)

    @Query("SELECT * FROM color_schemes")
    fun getAll() : List<ColorSchemeEntity>

    @Query("SELECT * FROM color_schemes WHERE id = :schemeId")
    fun getColorSchemeById(schemeId : Int) : ColorSchemeEntity
}


/**
 * u should use this by Color(ColorScheme.primary) // since the db only store long values
 */