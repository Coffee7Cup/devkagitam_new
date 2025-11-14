package com.yash.kagitam.db.api

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ApiEntity::class], version = 1, exportSchema = false)
abstract class ApiDB : RoomDatabase() {

    abstract fun apiDao() : ApiDao

    companion object{
        @Volatile
        private var INSTANCE : ApiDB? = null

        fun getDatabase(appCtx: Context) : ApiDB{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    appCtx.applicationContext,
                    ApiDB::class.java,
                    "api_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}