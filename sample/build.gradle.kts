plugins {
    id("com.android.library")
    kotlin("multiplatform")
}

kotlin{
    androidTarget()
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.kotlin.coroutines.core)
                api(projects.pictureSelector)
            }
        }
        val androidMain by getting {
            dependencies {
                api(libs.androidx.appcompat)
                implementation(libs.exifInterface)
                api(libs.pictureselector)
                api(libs.pictureselector.compress)
            }
        }
    }
}

android {
    compileSdk = 33
    namespace = "com.hellomr3.sample"
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
