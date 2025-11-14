import java.nio.file.Files
import java.nio.file.StandardCopyOption

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.yash.kagitam"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.yash.kagitam"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file(project.property("RELEASE_STORE_FILE") as String)
            storePassword = project.property("RELEASE_STORE_PASSWORD") as String
            keyAlias = project.property("RELEASE_KEY_ALIAS") as String
            keyPassword = project.property("RELEASE_KEY_PASSWORD") as String
        }
    }

    buildTypes {

        debug {
            buildConfigField("boolean","IS_DEV_BUILD","true")
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
        }

        release {
            signingConfig = signingConfigs.getByName("release")
            buildConfigField("boolean", "IS_DEV_BUILD", "false")
            isMinifyEnabled = false
            isDebuggable = true
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
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

}

dependencies {
    implementation(project(":deps"))
    ksp(libs.androidx.room.compiler)
}

tasks.register("copyDevApkFile"){
    dependsOn(":paper:createApk")

    doLast {
        // --- Locate the paper module's build output ---
        val paperProject = project(":paper")
        val paperBuildDir = paperProject.buildDir
        val zipDir = File(paperBuildDir, "plugin-zips")

        // Match the same name your :paper script uses
        val zipFile = zipDir.listFiles()
            ?.filter { it.name.endsWith(".zip") }
            ?.maxByOrNull { it.lastModified() }


        if (zipFile == null || !zipFile.exists()) {
            throw GradleException("❌ Plugin zip not found at ${zipFile?.absolutePath}")
        }

        // --- Destination inside app's assets ---
        val assetsDir = File(projectDir, "src/main/assets").apply {
            if (!exists()) mkdirs()
        }

        val destFile = File(assetsDir, "paper.zip")

        // Copy (overwrite if exists)
        Files.copy(
            zipFile.toPath(),
            destFile.toPath(),
            StandardCopyOption.REPLACE_EXISTING
        )

        println("✅ Copied plugin zip to: ${destFile.absolutePath}")
    }
}

tasks.matching { it.name == "preDebugBuild" }.configureEach {
    dependsOn("copyDevApkFile")
}
