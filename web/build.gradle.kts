import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

val kotlinVersion = "1.7.20-Beta"
val kotlinxHtmlVersion = "0.8.0"
val serializationVersion = "1.3.3"
val ktorVersion = "2.0.3"
val logbackVersion = "1.2.11"
val kotlinWrappersVersion = "1.0.0-pre.354"
val kmongoVersion = "4.5.0"

plugins {
    kotlin("multiplatform")
    application //to run JVM part
    kotlin("plugin.serialization") version "1.7.20-Beta"
}

group = "app.getsizzle.web"
version = "1.0"

dependencies {
    implementation("io.ktor:ktor-server-html-builder:2.1.3")
    implementation("org.webjars.npm:fortawesome__fontawesome-free:6.2.0")
    implementation("io.ktor:ktor-server-websockets:$ktorVersion")
    implementation("io.ktor:ktor-server-sessions:$ktorVersion")
    implementation("io.ktor:ktor-server-auth:$ktorVersion")
}

kotlin {
    jvm {
        withJava()
    }
    js(IR) {
        browser {
            binaries.executable()
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-html:0.8.0")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(project(":shared"))
                implementation("io.ktor:ktor-serialization:$ktorVersion")
                implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("io.ktor:ktor-server-cors:$ktorVersion")
                implementation("io.ktor:ktor-server-compression:$ktorVersion")
                implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
                implementation("io.ktor:ktor-server-netty:$ktorVersion")
                implementation("ch.qos.logback:logback-classic:$logbackVersion")
                implementation("org.litote.kmongo:kmongo-coroutine-serialization:$kmongoVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.8.0")
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(project(":shared"))
                implementation("io.ktor:ktor-client-js:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
//                implementation(project.dependencies.enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:$kotlinWrappersVersion"))
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom")
                implementation(project.dependencies.enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:1.0.0-pre.354"))
//                implementation(npm("@types/jquery","3.5.1", generateExternals = true))
                implementation(npm("jquery","3.5.1"))
                implementation("org.jetbrains.kotlin-wrappers:kotlin-css:1.0.0-pre.200-kotlin-1.5.0")
                implementation("org.jetbrains.kotlinx:kotlinx-html-js:0.8.0")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-router-dom")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-emotion")
            }
        }
    }
}

application {
    mainClass.set("app.getsizzle.web.ServerKt")
}

// include JS artifacts in any JAR we generate
tasks.getByName<Jar>("jvmJar") {
    val taskName = if (project.hasProperty("isProduction")
        || project.gradle.startParameter.taskNames.contains("installDist")
    ) {
        "jsBrowserProductionWebpack"
    } else {
        "jsBrowserDevelopmentWebpack"
    }
    val webpackTask = tasks.getByName<KotlinWebpack>(taskName)
    dependsOn(webpackTask) // make sure JS gets compiled first
    from(File(webpackTask.destinationDirectory, webpackTask.outputFileName)) // bring output file along into the JAR
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

distributions {
    main {
        contents {
            from("$buildDir/libs") {
                rename("${rootProject.name}-jvm", rootProject.name)
                into("lib")
            }
        }
    }
}

// Alias "installDist" as "stage" (for cloud providers)
tasks.create("stage") {
    dependsOn(tasks.getByName("installDist"))
}

tasks.getByName<JavaExec>("run") {
    classpath(tasks.getByName<Jar>("jvmJar")) // so that the JS artifacts generated by `jvmJar` can be found and served
}

tasks.withType(Tar::class){
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType(Zip::class){
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
