package com.yash.devkagitam.db.plugins

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yash.devkagitam.registries.AppRegistry

@Database(entities = [MetaDataPluginEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class MetaDataPluginDB : RoomDatabase() {
    abstract fun metaDataPluginDao(): MetaDataPluginDao

    companion object {
        @Volatile
        private var INSTANCE: MetaDataPluginDB? = null

        fun getDatabase(): MetaDataPluginDB {
            val context = AppRegistry.getAppContext()
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MetaDataPluginDB::class.java,
                    "MetaDataPlugin"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}