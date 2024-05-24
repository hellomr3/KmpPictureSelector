import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)

    id("com.vanniktech.maven.publish") version "0.28.0"
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


mavenPublishing {
    coordinates("io.github.hellomr3", "KmpPictureSelectorCompose", "0.0.1")
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
