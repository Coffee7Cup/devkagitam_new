package com.yash.kagitam.db.colorSchemes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ColorSchemeEntity::class], version = 1)
abstract class ColorSchemeDB : RoomDatabase() {
    abstract fun colorSchemeDao() : ColorSchemeDao

    companion object{
        @Volatile
        private var INSTANCE : ColorSchemeDB? = null

        fun getDataBase(appCtx: Context) : ColorSchemeDB{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    appCtx.applicationContext,
                    ColorSchemeDB::class.java,
                    "color_schemes"
                ).build()
                INSTANCE = instance
                instance
            }

        }
    }
}