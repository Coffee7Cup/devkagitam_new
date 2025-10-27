package com.yash.devkagitam.db.colorSchemes

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yash.devkagitam.registries.AppRegistry

@Database(entities = [ColorSchemeEntity::class], version = 1)
abstract class ColorSchemeDB : RoomDatabase() {
    abstract fun colorSchemeDao() : ColorSchemeDao

    companion object{
        @Volatile
        private var INSTANCE : ColorSchemeDB? = null

        fun getDB() : ColorSchemeDB{
            val context = AppRegistry.getAppContext()
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ColorSchemeDB::class.java,
                    "color_schemes"
                ).build()
                INSTANCE = instance
                instance
            }

        }
    }
}