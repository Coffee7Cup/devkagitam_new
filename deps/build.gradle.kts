plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.yash.deps"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        targetSdk = 36
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures{
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    buildTypes{
        debug {
            buildConfigField("boolean","IS_DEV_BUILD","true")
        }
        release {
            buildConfigField("boolean", "IS_DEV_BUILD", "false")
        }
    }
}

dependencies {

    // ===== Compose BOM =====
    api(platform(libs.androidx.compose.bom))

    // ===== Core Android =====
    api(libs.androidx.core.ktx)
    api(libs.androidx.appcompat)
    api(libs.material)

    // ===== Compose Core =====
    api(libs.androidx.ui)
    api(libs.androidx.ui.graphics)
    api(libs.androidx.ui.tooling)
    api(libs.androidx.ui.tooling.preview)
    api(libs.androidx.foundation)

    // ===== Compose Material =====
    api(libs.androidx.material3)
    api(libs.androidx.material.icons.extended)

    // ===== Activity + Lifecycle =====
    api(libs.androidx.activity.compose)
    api(libs.androidx.lifecycle.runtime.ktx)

    // ===== Navigation =====
    api(libs.androidx.navigation.compose)

    // ===== Accompanist =====
    api(libs.accompanist.pager)
    api(libs.accompanist.pager.indicators)

    // ===== Networking =====
    api(libs.okhttp)
    api(libs.retrofit)
    api(libs.retrofit.gson)

    // ===== Charts =====
    api(libs.charty)

    // ===== db module ======
    api(libs.androidx.room.runtime)
    api(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // ==== Gson ====
    api(libs.gson)

    // ===== Testing =====
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.test.manifest)
}