package com.yash.devkagitam.db.widgets

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface WidgetDao {

    @Query("SELECT * FROM $WIDGET_DB")
    suspend fun getAllWidgets(): List<WidgetEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(widget: WidgetEntity)

    @Update
    suspend fun update(widget: WidgetEntity)

    @Delete
    suspend fun delete(widget: WidgetEntity)

    @Query("SELECT * FROM $WIDGET_DB WHERE owner = :owner")
    suspend fun getWidgetsByOwner(owner: String): List<WidgetEntity>
}