buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${CommonVersions.kotlin}")
        classpath("com.android.tools.build:gradle:7.2.2")
        classpath ("com.google.gms:google-services:4.3.15") //Added for firebase
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
