plugins {
    id("com.android.application")
    kotlin("android")
    id("org.jetbrains.compose")
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
}
dependencies {
    implementation(projects.sample.shared)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.activity)
    implementation(libs.androidx.activity.compose)
}