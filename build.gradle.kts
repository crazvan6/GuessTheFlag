// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    id("com.android.library") version "7.2.2" apply false
}
buildscript{
    dependencies{
        classpath("com.android.tools.build:gradle:7.0.3")
        classpath ("com.google.gms:google-services:4.4.0")
    }
}
true // Needed to make the Suppress annotation work for the plugins block