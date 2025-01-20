plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    kotlin("kapt")
}

android {
    namespace = "com.kushal.mealapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.kushal.mealapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 3
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14" // Ensure compatibility with Kotlin 1.9.24
    }

}

dependencies {
    // Core libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.recyclerview)

    // Test libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Networking libraries
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.google.code.gson:gson:2.10.1") // Updated version
    implementation("com.squareup.okhttp3:okhttp:4.11.0") // Updated version
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0") // Updated version

    // Jetpack Compose
    implementation("androidx.compose.ui:ui:1.6.0") // Updated version
    implementation("androidx.compose.material:material:1.6.0") // Updated version
    implementation("androidx.compose.ui:ui-tooling:1.6.0") // Updated version
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.1")

    // Room Database
    implementation("androidx.room:room-runtime:2.6.0-alpha03") // Updated version
    kapt("androidx.room:room-compiler:2.6.0-alpha03") // Updated version
    implementation("androidx.room:room-ktx:2.6.0-alpha03") // Updated version

    // Lifecycle components
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
}
