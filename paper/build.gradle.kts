import org.gradle.api.tasks.bundling.Zip
import java.io.File
import groovy.json.JsonSlurper

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias (libs.plugins.kotlin.compose)
}

android {
    namespace = "com.yash.paper"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        targetSdk = 36
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures { compose=true }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

tasks.withType<Test>().configureEach {
    enabled = false
}

dependencies {
    compileOnly(project(":dev"))
}

val manifestFile = file("$projectDir/src/main/java/com/yash/paper/manifest.json")

val manifestData: Map<String, Any> = if (manifestFile.exists()) {
    JsonSlurper().parse(manifestFile) as Map<String, Any>
} else {
    mapOf()
}
val nameZip = manifestData["name"]?.toString() ?: project.name

tasks.register<Zip>("bundlePlugin") {
    dependsOn("assembleRelease")

    val apkDir = file("$buildDir/outputs/apk/release")

    from(apkDir) {
        include("*.apk")
        rename { "paper.apk" } // rename inside zip
    }

    from(manifestFile) {
        into("") // place manifest.json at root of zip
    }

    archiveFileName.set("$nameZip.zip")
    destinationDirectory.set(file("$rootDir/build/papers"))

    doFirst {
        println("📦 Bundling plugin: ${project.name}")
        if (!manifestFile.exists()) {
            throw GradleException("❌ manifest.json not found in ${projectDir}")
        }
    }
}