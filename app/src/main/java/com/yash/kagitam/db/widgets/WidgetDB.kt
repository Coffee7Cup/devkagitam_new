package com.yash.kagitam.db.widgets

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WidgetEntity::class], version = 1, exportSchema = false)
abstract class WidgetDB : RoomDatabase() {
    abstract fun widgetDao(): WidgetDao


    companion object {
        @Volatile
        private var INSTANCE: WidgetDB? = null

        fun getDatabase(appCtx: Context): WidgetDB {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    appCtx.applicationContext,
                    WidgetDB::class.java,
                    WIDGET_DB
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}