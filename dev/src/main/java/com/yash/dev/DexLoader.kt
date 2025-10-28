package com.yash.dev

import android.content.Context
import dalvik.system.DexClassLoader
import java.io.File

fun loadDex(appContext : Context, apkPath: String, classToLoad: String): Any {
    val apkFile = File(apkPath)

    if (!apkFile.exists()) {
        throw Error(
            "APK file not found at path: ${apkFile.absolutePath}"
        )
    }

    val optimizedDir = File(appContext.codeCacheDir, "plugin_opt")
    if (!optimizedDir.exists()) {
        optimizedDir.mkdirs()
    }

    val dexClassLoader = DexClassLoader(
        apkFile.absolutePath,
        optimizedDir.absolutePath,
        null,
        appContext.classLoader
    )

    val clazz = dexClassLoader.loadClass(classToLoad)
    return clazz.getDeclaredConstructor().newInstance()
}
