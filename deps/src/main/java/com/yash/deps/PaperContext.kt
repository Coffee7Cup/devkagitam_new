package com.yash.deps

import android.content.Context
import android.content.ContextWrapper
import androidx.room.RoomDatabase

/**
 * Public interface that plugin modules (papers) can use.
 * The host’s real PaperContext implements this.
 */
abstract class PaperContext(
    val paperName: String,
    base: Context
) : ContextWrapper(base) {

    abstract suspend fun getApiByOwnerName(owner: String): Any?
    abstract suspend fun navigateToThisPaper()
    abstract fun <T : RoomDatabase> buildRoomDatabase(dbClass: Class<T>, dbName: String): T
}