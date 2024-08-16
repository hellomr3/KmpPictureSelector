import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinMultiplatform)
    kotlin("native.cocoapods")
    id("com.vanniktech.maven.publish") version "0.28.0"
}

kotlin {
//    withSourcesJar(publish = false)
    cocoapods {
        version = "1.0.0"
        ios.deploymentTarget = "12.0"
        pod("TZImagePickerController/Basic") {
            version = "~> 3.8.5"
        }
    }
    androidTarget {

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
        }
        publishLibraryVariants("release", "debug")
//        publishLibraryVariantsGroupedByFlavor = true
    }
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { target ->
        target.binaries.framework {
            baseName = path.substring(1).replace(':', '-')
            isStatic = true
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
    namespace = "com.picture_selector"
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

}

group = "io.github.hellomr3"
version = "1.0.0"

mavenPublishing {
    coordinates("io.github.hellomr3", "KmpPictureSelector", "0.0.1")
    // sources publishing is always enabled by the Kotlin Multiplatform plugin
    configure(
        KotlinMultiplatform(
            // configures the -javadoc artifact, possible values:
            // - `JavadocJar.None()` don't publish this artifact
            // - `JavadocJar.Empty()` publish an emprt jar
            // - `JavadocJar.Dokka("dokkaHtml")` when using Kotlin with Dokka, where `dokkaHtml` is the name of the Dokka task that should be used as input
            javadocJar = JavadocJar.Empty(),
            // whether to publish a sources jar
            sourcesJar = true,
            // configure which Android library variants to publish if this project has an Android target
            // defaults to "release" when using the main plugin and nothing for the base plugin
            androidVariantsToPublish = listOf("debug", "release"),
        )
    )
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
//    // or when publishing to https://s01.oss.sonatype.org
//    publishToMavenCentral(SonatypeHost.S01)
//    // or when publishing to https://central.sonatype.com/
//    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    pom {
        name.set("KmpPictureSelector")
        description.set("a kotlin mutiplatform picture selector for android or ios")
        inceptionYear.set("2024")
        url.set("https://github.com/hellomr3/KmpPictureSelector/")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("hellomr3")
                name.set("hellomr3")
                url.set("https://github.com/hellomr3")
            }
        }
        scm {
            url.set("https://github.com/hellomr3/KmpPictureSelector/")
            connection.set("scm:git:git://github.com/hellomr3/KmpPictureSelector.git")
            developerConnection.set("scm:git:ssh://git@github.com/hellomr3/KmpPictureSelector.git")
        }
    }
}