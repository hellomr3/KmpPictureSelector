plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)

    id("maven-publish")
}

kotlin{
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
                implementation(compose.foundation)
                api(projects.pictureSelector)
            }
        }
        val androidMain by getting{
            dependencies{
                implementation(libs.androidx.appcompat)
            }
        }
    }
}

android {
    compileSdk = 33
    namespace = "com.picture_selector.compose"

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