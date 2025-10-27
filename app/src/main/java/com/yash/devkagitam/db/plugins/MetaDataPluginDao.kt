package com.yash.devkagitam.db.plugins

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface MetaDataPluginDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(plugin: MetaDataPluginEntity)

    @Update
    suspend fun update(plugin: MetaDataPluginEntity)

    @Delete
    suspend fun delete(plugin: MetaDataPluginEntity)

    @Query("SELECT * FROM MetaDataPlugin")
    suspend fun getAllPlugins(): List<MetaDataPluginEntity>

    @Query("DELETE FROM MetaDataPlugin WHERE name = :pluginName")
    suspend fun deleteByName(pluginName: String): Int

    @Query("SELECT * FROM MetaDataPlugin WHERE name = :name LIMIT 1")
    suspend fun getPluginByName(name: String): MetaDataPluginEntity?

    @Query("SELECT widgets FROM MetaDataPlugin")
    suspend fun getAllWidgets() : List<String>

}
