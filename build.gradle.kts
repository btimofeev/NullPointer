// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    extra.apply{
        set("minSdkVersion", 29)
        set("compileSdkVersion", 34)
    }
}

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.jetbrainsKotlinJvm) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.ksp) apply false
}