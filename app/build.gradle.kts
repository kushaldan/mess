plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.recyclerview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Retrofit dependencies
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Volley dependency (if needed)
    implementation("com.android.volley:volley:1.2.1")

    // Gson dependency (if not using the version in the Retrofit dependency)
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.11")

}
