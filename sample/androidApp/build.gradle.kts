plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.hellomr3.sample"
    compileSdk = 34
    defaultConfig {
        minSdk = 21
    }
    defaultConfig {
        applicationId = "com.hellomr3.sample"

        versionCode = 1
        versionName = "1.0.0"
    }

    buildTypes {
        debug {
            isDebuggable = true
            version = 1
            signingConfig = signingConfigs.getByName("debug")
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
        dataBinding = true
    }
}
dependencies {
    implementation(projects.sample)
    implementation(libs.androidx.appcompat)
}