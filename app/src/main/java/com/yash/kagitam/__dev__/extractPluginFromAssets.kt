package com.yash.kagitam.__dev__

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

fun extractPluginFromAssets(context: Context, assetName: String, outputDirName: String): File {
    val destDir = File(context.filesDir, outputDirName)
    if (destDir.exists()) destDir.deleteRecursively()
    destDir.mkdirs()

    context.assets.open(assetName).use { input ->
        ZipInputStream(input).use { zis ->
            var entry: ZipEntry?
            while (zis.nextEntry.also { entry = it } != null) {
                val newFile = File(destDir, entry!!.name)
                if (entry!!.isDirectory) {
                    newFile.mkdirs()
                } else {
                    newFile.parentFile?.mkdirs()
                    FileOutputStream(newFile).use { fos ->
                        zis.copyTo(fos)
                    }
                }
                zis.closeEntry()
            }
        }
    }

    destDir.setReadable(true, false)
    destDir.setWritable(true, false)

    Log.d("EPFA","✅ Extracted plugin to: ${destDir.absolutePath}")
    return destDir
}
