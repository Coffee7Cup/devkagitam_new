package com.yash.kagitam.db.plugins

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [MetaDataPluginEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class MetaDataPluginDB : RoomDatabase() {
    abstract fun metaDataPluginDao(): MetaDataPluginDao

    companion object {
        @Volatile
        private var INSTANCE: MetaDataPluginDB? = null

        fun getDatabase(appCtx: Context): MetaDataPluginDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    appCtx.applicationContext,
                    MetaDataPluginDB::class.java,
                    "MetaDataPlugin"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}