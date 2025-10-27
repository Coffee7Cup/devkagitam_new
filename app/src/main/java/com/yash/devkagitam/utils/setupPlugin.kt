package com.yash.devkagitam.utils

import com.google.gson.Gson
import com.yash.devkagitam.db.api.ApiDB
import com.yash.devkagitam.db.api.ApiEntity
import com.yash.devkagitam.db.plugins.MetaDataPluginDB
import com.yash.devkagitam.db.plugins.MetaDataPluginEntity
import com.yash.devkagitam.db.widgets.WidgetDB
import com.yash.devkagitam.db.widgets.WidgetEntity
import java.io.File
import java.io.FileReader

data class MetaDataFile(
  val name: String,
  val author: String,
  val version: String,
  val id: String,
  val entryPoint: String,
  val widgets: List<String>,
  val apiClass: String,
  val apiInterface: String?
)

suspend fun setupPlugin(parent_path: String, name: String) {

  val path = unzipPaper(
      parent_path,
      name = name
  )

  //Unzip the file here---very important-------

 try {
   val metaFile = File(path, "metaData.json")
   val fileMeta = Gson().fromJson(FileReader(metaFile), MetaDataFile::class.java)

   // Save metadata, widgets, APIs etc.
   val pluginMeta = MetaDataPluginEntity(
     name = fileMeta.name,
     author = fileMeta.author,
     version = fileMeta.version,
     id = fileMeta.id,
     entryPoint = fileMeta.entryPoint,
     widgets = fileMeta.widgets,
     apiClass = fileMeta.apiClass,
     path = path,
   )

   MetaDataPluginDB.getDatabase()
     .metaDataPluginDao()
     .insert(pluginMeta)

   val widgetDao = WidgetDB.getDatabase().widgetDao()
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

   val apiDao = ApiDB.getDatabase().apiDao()
   apiDao.insert(
     ApiEntity(
       owner = fileMeta.name,
       apiClass = fileMeta.apiClass,
       apkPath = path
     )
   )
 }catch(e : Error){
   File(path).deleteRecursively()
   throw e
 }
}