// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://www.jitpack.io") }
        gradlePluginPortal()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:8.4.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.22")
    }
}

tasks {
    register("clean", Delete::class) {
        delete(rootProject.layout.buildDirectory)
    }
}
