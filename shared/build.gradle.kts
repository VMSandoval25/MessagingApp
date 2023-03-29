val kotlinxHtmlVersion = "0.8.0"
val serializationVersion = "1.3.3"
val ktorVersion = "2.0.3"
val logbackVersion = "1.2.11"
val kmongoVersion = "4.5.0"

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization") version CommonVersions.kotlin
    id(CommonPlugins.ksp) version CommonVersions.ksp
    id(CommonPlugins.nativeCoroutines) version CommonVersions.nativeCoroutines
}

group = "app.getsizzle.shared"
version = "1.0"

android {
    compileSdk = 33
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 26
        targetSdk = 33
    }
    namespace = "app.getsizzle.shared"
}

kotlin {
    jvm()
    js(IR) {
        browser()
    }
    android()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries {
            framework {
                baseName = "shared"
                export(CommonDeps.kotlinDateTime)
            }
        }
    }

    sourceSets {
        all {
            languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
        }

        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                implementation ("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-json:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")
                implementation("co.touchlab:kermit:1.2.2")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmMain by getting
        val jsMain by getting
        val androidMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation ("io.ktor:ktor-client-okhttp:2.1.2")
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation("io.ktor:ktor-client-darwin:$ktorVersion")
            }
        }
    }
}
