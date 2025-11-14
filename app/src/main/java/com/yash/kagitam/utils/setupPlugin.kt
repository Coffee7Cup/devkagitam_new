package com.yash.kagitam.utils

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.yash.kagitam.db.api.ApiDB
import com.yash.kagitam.db.api.ApiEntity
import com.yash.kagitam.db.plugins.MetaDataPluginDB
import com.yash.kagitam.db.plugins.MetaDataPluginEntity
import com.yash.deps.unzip
import com.yash.kagitam.db.widgets.WidgetDB
import com.yash.kagitam.db.widgets.WidgetEntity
import com.yash.kagitam.registries.AppRegistry
import java.io.File
import java.io.FileReader

data class MetaDataFile(
  val name: String,
  val author: String,
  val version: String,
  val id: String,
  val entryPoint: String,
  val widgets: List<String>,
  val apiClass: String?,
  val apiInterface: String?
)

const val PAPERS = "papers"

suspend fun setupPlugin(parent_path: String, name: String) {

  val appCtx = AppRegistry.getAppContext()

  val execDir = appCtx.getDir("papers", Context.MODE_PRIVATE)
  val path = "${execDir.absolutePath}/$name"

  unzip(
    zipFilePath = parent_path,
    saveFileAt = path,
  )

  try {
    val metaFile = File(path, "manifest.json")
    val fileMeta = Gson().fromJson(FileReader(metaFile), MetaDataFile::class.java)

    // Save metadata, widgets, APIs etc.
    val pluginMeta = MetaDataPluginEntity(
      name = fileMeta.name,
      author = fileMeta.author,
      version = fileMeta.version,
      entryPoint = fileMeta.entryPoint,
      widgets = fileMeta.widgets,
      apiClass = fileMeta.apiClass,
      path = path,
    )

    MetaDataPluginDB.getDatabase(appCtx)
      .metaDataPluginDao()
      .insert(pluginMeta)

    val widgetDao = WidgetDB.getDatabase(appCtx).widgetDao()
    fileMeta.widgets.forEach { widgetClass ->
      widgetDao.insert(
        WidgetEntity(
          owner = fileMeta.name,
          widgetClass = widgetClass,
          apkPath = path,
          selected = false
        )
      )
    }

    // Insert API entry only if apiClass and apiInterface are not null or blank
    val apiClass = fileMeta.apiClass?.trim()
    val apiInterface = fileMeta.apiInterface?.trim()
    if (!apiClass.isNullOrEmpty() && !apiInterface.isNullOrEmpty()) {
      val apiDao = ApiDB.getDatabase(appCtx).apiDao()
      apiDao.insert(
        ApiEntity(
          owner = fileMeta.name,
          apiClass = apiClass,
          apkPath = path
        )
      )
    }

  } catch (e: Error) {
    File(path).deleteRecursively()
    throw e
  }
}
