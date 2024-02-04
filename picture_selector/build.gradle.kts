plugins {
    alias(libs.plugins.androidLibrary)
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("maven-publish")
}

kotlin{
//    withSourcesJar(publish = false)
    cocoapods {
        version = "1.0.0"
        ios.deploymentTarget = "12.0"
        pod("TZImagePickerController/Basic") {
            version = "~> 3.8.4"
        }
    }
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
        publishLibraryVariants("release", "debug")
//        publishLibraryVariantsGroupedByFlavor = true
    }
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.kotlin.coroutines.core)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.appcompat)
                implementation(libs.exifInterface)
                implementation(libs.pictureselector)
                implementation(libs.pictureselector.compress)
                implementation(libs.pictureselector.ucrop)
                implementation(libs.coil3)
                implementation(libs.coil3.network)
                implementation(libs.ktor.client.android)
            }
        }
    }
}

android {
    compileSdk = 34
    namespace = "com.usecase.picture_selector"
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

}

group="com.hellomr3"
version = "1.0.0"

publishing {
    publishing{
        repositories {
            mavenLocal()
        }
    }
}