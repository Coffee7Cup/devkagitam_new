package com.yash.kagitam.utils

import android.R
import android.content.Context
import android.content.SharedPreferences
import android.content.res.AssetManager
import android.content.res.Resources
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yash.deps.PaperContext
import com.yash.kagitam.db.api.ApiDB
import com.yash.kagitam.db.plugins.MetaDataPluginDB
import com.yash.kagitam.registries.AppRegistry
import com.yash.kagitam.registries.PaperInstanceRegistry
import java.io.File

class PaperContextImpl(
    paperName: String,
    base: Context
) :  PaperContext(paperName, base) {

    /** ---------------- Resources & Assets ---------------- */

    private val assetManager: AssetManager
    private val resources: Resources
    private val theme: Resources.Theme
    private val pkgName: String

    init {
        /**
         * Bro change this in build and debug
         *
         * i mean in dubut i have libery and ib build i have appilcation
         */
        val provided = File(base.filesDir.absolutePath, "papers/$paperName")

        val apkFile: File = when {
            provided.isDirectory -> {
                provided.listFiles()?.firstOrNull { it.name.endsWith(".apk") }
                    ?: throw Error("No .apk found inside directory: ${provided.absolutePath}")
            }
            else -> provided
        }

        apkFile.setReadable(true, false)
        apkFile.setWritable(false, false)

        assetManager = AssetManager::class.java.newInstance().apply {
            javaClass.getMethod("addAssetPath", String::class.java).invoke(this, apkFile.absolutePath)
        }

        val hostRes = base.resources
        resources = Resources(assetManager, hostRes.displayMetrics, hostRes.configuration)
        theme = resources.newTheme().apply {
            applyStyle(R.style.Theme_DeviceDefault, true)
        }

        val pkgInfo = base.packageManager.getPackageArchiveInfo(apkFile.absolutePath, 0)
        pkgName = pkgInfo?.packageName ?: "com.plugin.$paperName"
        //TODO add the BuildConfig...for debug

    }

    override fun getResources() = resources
    override fun getAssets() = assetManager
    override fun getTheme() = theme
    override fun getPackageName() = pkgName


    override fun getSystemService(name: String): Any? {
        return baseContext.getSystemService(name)
    }


    /** ---------------- Lock down escape routes ---------------- */

    // Prevent plugins from jumping back to host app context
    override fun getApplicationContext(): Context {
        return this
    }

    override fun getBaseContext(): Context {
        return this
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
        val dir = File(baseContext.filesDir, "papers/$paperName/files")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    override fun getCacheDir(): File {
        val dir = File(baseContext.cacheDir, "papers/$paperName/cache")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    fun getPluginRootDir(): File {
        val dir = File(baseContext.filesDir, "papers/$paperName")
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
        factory: SQLiteDatabase.CursorFactory?
    ): SQLiteDatabase {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory)
    }

    /** ---------------- SharedPreferences ---------------- */
    override fun getSharedPreferences(name: String?, mode: Int): SharedPreferences {
        val prefName = "${paperName}_$name"
        return super.getSharedPreferences(prefName, mode)
    }

    /**-----------------for api-------------------------- */
    //the below returning null => no api exists

    override suspend fun getApiByOwnerName(owner: String): Any? {
        ApiDB.getDatabase(baseContext).apiDao().getApiByOwner(owner)?.let{
            return PaperInstanceRegistry.getApiInstance(baseContext, it)
        }
        return null
    }

    override suspend fun navigateToThisPaper(){
        val navController = AppRegistry.getNavController()

        MetaDataPluginDB.getDatabase(baseContext).metaDataPluginDao().getPluginByName(paperName)?.let{
            val instance = PaperInstanceRegistry.getPaperInstance(baseContext, it)

            AppRegistry.setCurrentPlugin(instance, paperName)

            navController.navigate("paper"){
                launchSingleTop = true
            }
        } ?: throw Throwable("something went wrong")

    }


    /** ---------------- Room DB ---------------- */
    override fun <T : RoomDatabase> buildRoomDatabase(
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