plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.gms.google-services") // Firebase
}

android {
    namespace = "app.getsizzle.messaging"
    compileSdk = 33
    buildToolsVersion = "33.0.0"

    defaultConfig {
        applicationId = "app.getsizzle.messaging"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    composeOptions {
        kotlinCompilerExtensionVersion = AndroidVersions.composeCompiler
    }

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=all", "-opt-in=kotlin.RequiresOptIn")
    }
    sourceSets {
        getByName("main").res.srcDir(
            "src/main/res"
        )
    }

    kotlin {
        sourceSets["main"].apply {
            kotlin.srcDir("src/main/kotlin")
            //kotlin.srcDir("src/main/res/font")
        }
    }

}



dependencies {
    implementation(project(":shared"))
    implementation(platform("com.google.firebase:firebase-bom:31.2.2"))
    implementation ("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("androidx.work:work-runtime-ktx:2.8.0")
    implementation ("com.google.firebase:firebase-analytics")
    //implementation("com.google.android.material3:material")
    implementation("androidx.compose.material3:material3:1.0.1")
    implementation ("androidx.compose.material3:material3-window-size-class:1.0.1")






    // AndroidX
    implementation(AndroidDeps.AndroidX.windowManager)
    implementation(AndroidDeps.AndroidX.Lifecycle.liveDataCore)
    implementation(AndroidDeps.AndroidX.Lifecycle.liveDataKtx)
    implementation(AndroidDeps.AndroidX.Lifecycle.runtime)

    // Compose
    implementation(AndroidDeps.AndroidX.Compose.ui)
    implementation(AndroidDeps.AndroidX.Compose.uiUtil)
    implementation(AndroidDeps.AndroidX.Compose.material)
    implementation(AndroidDeps.AndroidX.Compose.materialIcons)
    implementation(AndroidDeps.AndroidX.Compose.uiTooling)
    implementation(AndroidDeps.AndroidX.Compose.uiToolingPreview)
    implementation("androidx.compose.foundation:foundation:1.4.0-beta01")
//    implementation(AndroidDeps.AndroidX.Compose.foundation)
    implementation(AndroidDeps.AndroidX.Compose.activity)
    implementation(AndroidDeps.AndroidX.Compose.liveDataRuntime)
    implementation(AndroidDeps.AndroidX.Compose.lifecycleViewModel)
    implementation(AndroidDeps.AndroidX.Compose.navigationCommon) {
        version {
            strictly(AndroidVersions.navigationCompose)
        }
    }
    implementation(AndroidDeps.AndroidX.Compose.navigation){
        version {
            strictly(AndroidVersions.navigationCompose)
        }
    }
    implementation(AndroidDeps.AndroidX.Compose.constraintLayout)

    // Accompanist
    implementation(AndroidDeps.Accompanist.insets)
    implementation(AndroidDeps.Accompanist.insetsUi)
    implementation(AndroidDeps.Accompanist.systemUiController)
    implementation(AndroidDeps.Accompanist.pagerIndicator)
    implementation(AndroidDeps.Accompanist.pager)
    implementation(AndroidDeps.Accompanist.navigation)
    implementation(AndroidDeps.Accompanist.navigationAnimation)
    implementation(AndroidDeps.Accompanist.flowLayout)

    // Coroutines
    implementation(AndroidDeps.Coroutines.core) {
        version {
            strictly(CommonVersions.coroutines)
        }
    }
    implementation(AndroidDeps.Coroutines.android) {
        version {
            strictly(CommonVersions.coroutines)
        }
    }

    // Coil
    implementation(AndroidDeps.Coil.core)
    implementation(AndroidDeps.Coil.compose)
    implementation(AndroidDeps.Coil.svg)
}

kapt {
    correctErrorTypes = true
}
