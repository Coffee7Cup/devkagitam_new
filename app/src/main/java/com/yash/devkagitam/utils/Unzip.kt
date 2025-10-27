package com.yash.devkagitam.utils

import com.yash.devkagitam.registries.AppRegistry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipInputStream

suspend fun unzipPaper(zipFilePath: String, name: String): String = withContext(Dispatchers.IO) {

    val appContext = AppRegistry.getAppContext()

    // Create plugin output directory
    val outputDir = File(appContext.filesDir, "papers/$name")
    outputDir.mkdirs()

    val zipFile = File(zipFilePath)
    if (!zipFile.exists()) {
        throw Error("Zip file not found at path: $zipFilePath")
    }

    // Extract files
    ZipInputStream(zipFile.inputStream()).use { zipInputStream ->
        var entry = zipInputStream.nextEntry
        while (entry != null) {
            val file = File(outputDir, entry.name)
            if (entry.isDirectory) {
                file.mkdirs()
            } else {
                file.parentFile?.mkdirs()
                FileOutputStream(file).use { out ->
                    zipInputStream.copyTo(out)
                }
            }
            zipInputStream.closeEntry()
            entry = zipInputStream.nextEntry
        }
    }

    return@withContext outputDir.absolutePath
}
