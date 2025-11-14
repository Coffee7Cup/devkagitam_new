import org.gradle.api.tasks.bundling.Zip
import groovy.json.JsonSlurper
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import java.io.FileOutputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.yash.paper"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        targetSdk = 36
        // applicationId must be set if building as an application
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions { jvmTarget = "17" }

    buildFeatures { compose = true }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}


dependencies {
    compileOnly(project(":deps"))
}

// ----------------- Load Manifest -----------------
val manifestFile = file("$projectDir/src/main/java/com/yash/paper/manifest.json")

val manifestData: Map<String, Any> = if (manifestFile.exists()) {
    JsonSlurper().parseText(manifestFile.readText()) as Map<String, Any>
} else mapOf()

val nameZip = manifestData["name"]?.toString() ?: project.name

tasks.register("createApk") {
    dependsOn("assembleRelease")

    doLast {
        // Use the build directory name defined by the manifest
        val pluginDir = file("$buildDir/personal-builds/$nameZip").apply {
            deleteRecursively()
            mkdirs()
        }

        // Find the generated APK file
        val apkFile = fileTree("$buildDir/outputs/apk/release").matching {
            include("*.apk")
        }.singleFile

        // Copy files into the temporary plugin directory
        apkFile.copyTo(File(pluginDir, "paper.apk"), overwrite = true)
        if (manifestFile.exists()) manifestFile.copyTo(File(pluginDir, "manifest.json"), overwrite = true)

        println("🎁 Plugin folder bundled at: ${pluginDir.absolutePath}")

        // Create the final ZIP archive
        val zipDir = file("$buildDir/plugin-zips").apply { mkdirs() }
        val zipFile = File(zipDir, "$nameZip.zip")

        // Use standard Java IO for zipping
        ZipOutputStream(FileOutputStream(zipFile)).use { zos ->
            pluginDir.walkTopDown().filter { it.isFile }.forEach { file ->
                val entryName = file.relativeTo(pluginDir).path.replace("\\", "/")
                zos.putNextEntry(ZipEntry(entryName))
                file.inputStream().copyTo(zos)
                zos.closeEntry()
            }
        }

        println("✅ Zipped plugin package: ${zipFile.absolutePath}")
    }
}