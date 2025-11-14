package com.yash.deps

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipInputStream

fun unzip(zipFilePath: String, saveFileAt: String){

    // Create plugin output directory
    val outputDir = File(saveFileAt)
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
}
