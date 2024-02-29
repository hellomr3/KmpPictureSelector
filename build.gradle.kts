plugins {
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.androidApplication).apply(false)
    alias(libs.plugins.jetbrainsCompose).apply(false)
    kotlin("android").version("1.9.21").apply(false)
    kotlin("multiplatform").version("1.9.21").apply(false)
    kotlin("native.cocoapods").version("1.9.21").apply(false)
}
