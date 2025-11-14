package com.yash.deps

import android.content.Context
import android.os.Build
import dalvik.system.DexClassLoader
import java.io.File

fun loadDex(appContext: Context, apkPath: String, classToLoad: String): Any {
    val provided = File(apkPath)
    val apkFile: File = when {
        provided.isDirectory -> {
            provided.listFiles()?.firstOrNull { it.name.endsWith(".apk") || it.name.endsWith(".jar") }
                ?: throw Error("No .apk/.jar found inside directory: ${provided.absolutePath}")
        }
        else -> provided
    }

    apkFile.setReadable(true, false)
    apkFile.setWritable(false, false)

    val optimizedDir = appContext.getDir("plugin_opt", Context.MODE_PRIVATE)
    if (!optimizedDir.exists()) optimizedDir.mkdirs()

    val dexClassLoader = DexClassLoader(
        apkFile.absolutePath,
        optimizedDir.absolutePath,
        null,
        appContext.classLoader
    )

    val clazz = dexClassLoader.loadClass(classToLoad)
    return clazz.getDeclaredConstructor().newInstance()
}
