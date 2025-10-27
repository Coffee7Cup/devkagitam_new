plugins {
    id("com.android.library")
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

    implementation(project(":app"))

    // Align with BOM
    compileOnly(platform(libs.androidx.compose.bom))
    compileOnly(libs.androidx.ui)
    compileOnly(libs.androidx.ui.tooling.preview)
    compileOnly(libs.androidx.material3)
    compileOnly(libs.androidx.activity.compose)
    compileOnly(libs.androidx.lifecycle.runtime.ktx)

    // Optional UI helpers
    compileOnly(libs.androidx.material.icons.extended)
    compileOnly(libs.accompanist.pager)
    compileOnly(libs.accompanist.pager.indicators)

    // Non-UI deps
    compileOnly(libs.gson)
    compileOnly(libs.charty)
    compileOnly(libs.okhttp)
    compileOnly(libs.retrofit)
    compileOnly(libs.retrofit.gson)
    compileOnly(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    compileOnly(libs.androidx.room.ktx)
}
