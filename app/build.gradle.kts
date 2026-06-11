import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
        languageVersion.set(KotlinVersion.KOTLIN_2_3)
        apiVersion.set(KotlinVersion.KOTLIN_2_3)
        freeCompilerArgs.addAll(
            "-XXLanguage:+ContextParameters",
            "-XXLanguage:+ExplicitBackingFields",
            "-opt-in=kotlin.uuid.ExperimentalUuidApi",
            "-opt-in=androidx.compose.foundation.style.ExperimentalFoundationStyleApi",
            "-opt-in=androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi",
            "-opt-in=androidx.compose.foundation.layout.ExperimentalLayoutApi",
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-opt-in=androidx.compose.material.ExperimentalMaterialApi"
        )
    }
}

android {
    namespace = "com.hearout.app"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.hearout.app"
        minSdk = 24
        targetSdk = 37
        versionCode = 12
        versionName = "1.12"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isDebuggable = true
            applicationIdSuffix = ".debug"
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        compose = true
    }
    packaging  {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }

    bundle {
        language {
            enableSplit = false
        }
    }
}

dependencies {

    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation (platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.core.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //translate
    implementation(libs.translate)

    //icons
    implementation(libs.androidx.material.icons.extended)

    // navigation
    implementation(libs.androidx.navigation.compose)

    // lifecycle compose
    implementation(libs.androidx.lifecycle.runtime.compose)

    //splash screen
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.app.update.ktx)
    implementation(libs.review.ktx)
    implementation(libs.kotlinx.collections.immutable)

    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
}