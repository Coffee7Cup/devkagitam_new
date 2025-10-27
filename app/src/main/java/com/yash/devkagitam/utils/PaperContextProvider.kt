package com.yash.devkagitam.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.content.res.AssetManager
import android.content.res.Resources
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yash.devkagitam.registries.AppRegistry
import java.io.File

class PaperContextProvider(
    private val paperName: String,
    base: Context = AppRegistry.getAppContext() // host app context
) : ContextWrapper(base) {

    /** ---------------- Resources & Assets ---------------- */

    private var paperResources: Resources? = null

    init {
        val assetManager = AssetManager::class.java.newInstance()
        val addAssetPath = assetManager.javaClass.getMethod("addAssetPath", String::class.java)
        addAssetPath.invoke(assetManager, "${base.filesDir}/plugins/$paperName")

        val superRes = base.resources
        paperResources = Resources(assetManager, superRes.displayMetrics, superRes.configuration)
    }

    override fun getResources(): Resources {
        return paperResources ?: super.getResources()
    }

    /** ---------------- Lock down escape routes ---------------- */

    // Prevent plugins from jumping back to host app context
    override fun getApplicationContext(): Context {
        return this
    }

    override fun getBaseContext(): Context {
        return this
    }

    override fun getPackageName(): String {
        // Give a fake package name for plugin isolation
        return "com.yash.$paperName"
    }

    override fun getPackageResourcePath(): String {
        // Plugin's sandbox path
        return getPluginRootDir().absolutePath
    }

    override fun getPackageCodePath(): String {
        return getPluginRootDir().absolutePath
    }

    /** ---------------- Files & Cache ---------------- */
    override fun getFilesDir(): File {
        val dir = File(baseContext.filesDir, "plugins/$paperName/files")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    override fun getCacheDir(): File {
        val dir = File(baseContext.cacheDir, "plugins/$paperName/cache")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    fun getPluginRootDir(): File {
        val dir = File(baseContext.filesDir, "plugins/$paperName")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    /** ---------------- Databases ---------------- */
    override fun getDatabasePath(name: String): File {
        val dbDir = File(getPluginRootDir(), "databases")
        if (!dbDir.exists()) dbDir.mkdirs()
        return File(dbDir, name)
    }

    override fun openOrCreateDatabase(
        name: String,
        mode: Int,
        factory: android.database.sqlite.SQLiteDatabase.CursorFactory?
    ): android.database.sqlite.SQLiteDatabase {
        return android.database.sqlite.SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory)
    }

    /** ---------------- SharedPreferences ---------------- */
    override fun getSharedPreferences(name: String?, mode: Int): SharedPreferences {
        val prefName = "${paperName}_$name"
        return super.getSharedPreferences(prefName, mode)
    }

    /** ---------------- Room DB ---------------- */
    fun <T : RoomDatabase> buildRoomDatabase(
        dbClass: Class<T>,
        dbName: String
    ): T {
        return Room.databaseBuilder(
            this, // plugin context
            dbClass,
            "${paperName}_$dbName"
        ).build()
    }
}
