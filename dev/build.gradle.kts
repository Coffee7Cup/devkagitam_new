plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.yash.dev"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        targetSdk = 36
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    api(libs.gson)

    // ===== Room =====
    api(libs.androidx.room.runtime)
    api(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // ===== Charts =====
    api(libs.charty)

    // ===== Testing =====
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.test.manifest)
}