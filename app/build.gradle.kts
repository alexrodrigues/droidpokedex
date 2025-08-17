plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.paparazzi)
    kotlin("kapt")
}

android {
    namespace = "com.rodriguesalex.droidpokedex"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.rodriguesalex.droidpokedex"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
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
        compose = true
    }
}



dependencies {

    implementation(project(":network"))
    implementation(project(":home:data"))
    implementation(project(":home:domain"))
    
    // Flutter module dependencies (including only necessary native libraries)
    debugImplementation("com.example.flutter_module:flutter_debug:1.0") {
        exclude(group = "io.flutter", module = "x86_64_debug")
        exclude(group = "io.flutter", module = "armeabi_v7a_debug")
        exclude(group = "io.flutter", module = "x86_debug")
        // Keep arm64_v8a_debug for your device architecture
    }
    releaseImplementation("com.example.flutter_module:flutter_release:1.0") {
        exclude(group = "io.flutter", module = "x86_64_release")
        exclude(group = "io.flutter", module = "armeabi_v7a_release")
        exclude(group = "io.flutter", module = "x86_release")
        // Keep arm64_v8a_release for your device architecture
    }

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.storage)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    implementation(libs.lifecycle.livedata.ktx)

    // UI Libs
    implementation(libs.coil.compose)

    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Retrofit
    implementation(libs.retrofit.lib)
    implementation(libs.retrofit.converter.gson)
}

kapt {
    correctErrorTypes = true
}
