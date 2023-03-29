object CommonVersions {
    const val kotlin = "1.8.10"
    const val ktor = "2.1.2"
    const val jUnit = "4.13.2"
    const val ksp = "1.8.10-1.0.9"
    const val kotlinDateTime = "0.4.0"
    const val kermitLogging = "1.1.3"
    const val mokoResources = "0.20.1"
    const val koin = "3.2.2"
    const val koinAnnotations = "1.0.3"
    const val coroutines = "1.6.4"
    const val nativeCoroutines = "0.13.3"
}

object CommonDeps {
    const val kotlinDateTime = "org.jetbrains.kotlinx:kotlinx-datetime:${CommonVersions.kotlinDateTime}"
    const val kermitLogging = "co.touchlab:kermit:${CommonVersions.kermitLogging}"

    object Koin {
        const val core = "io.insert-koin:koin-core:${CommonVersions.koin}"
        const val test = "io.insert-koin:koin-test:${CommonVersions.koin}"
        const val annotations = "io.insert-koin:koin-annotations:${CommonVersions.koinAnnotations}"
    }
}

object CommonPlugins {
    const val kotlinSerialization = "plugin.serialization"
    const val mokoResources = "dev.icerock.mobile.multiplatform-resources"
    const val ksp = "com.google.devtools.ksp"
    const val nativeCoroutines = "com.rickclephas.kmp.nativecoroutines"
}

object AndroidVersions {
    const val compose = "1.3.1"
    const val composeCompiler = "1.4.3"
    const val activityCompose = "1.6.0"
    const val accompanist = "0.26.5-rc"
    const val windowManager = "1.0.0"
    const val lifecycle = "2.5.1"
    const val lifecycleViewModelCompose = lifecycle
    const val navigationCompose = "2.5.2"
    const val coil = "2.2.2"
    const val constraintLayout = "1.0.1"
}

object AndroidDeps {

    object AndroidX {
        const val windowManager = "androidx.window:window:${AndroidVersions.windowManager}"

        object Lifecycle {
            const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:${AndroidVersions.lifecycle}"
            const val liveDataCore = "androidx.lifecycle:lifecycle-livedata-core-ktx:${AndroidVersions.lifecycle}"
            const val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:${AndroidVersions.lifecycle}"
        }

        object Compose {
            const val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:${AndroidVersions.lifecycleViewModelCompose}"
            const val navigation = "androidx.navigation:navigation-compose:${AndroidVersions.navigationCompose}"
            const val navigationCommon = "androidx.navigation:navigation-common-ktx:${AndroidVersions.navigationCompose}"
            const val ui = "androidx.compose.ui:ui:${AndroidVersions.compose}"
            const val uiUtil = "androidx.compose.ui:ui-util:${AndroidVersions.compose}"
            const val material = "androidx.compose.material:material:${AndroidVersions.compose}"
            const val materialIcons = "androidx.compose.material:material-icons-extended:${AndroidVersions.compose}"
            const val uiTooling = "androidx.compose.ui:ui-tooling:${AndroidVersions.compose}"
            const val uiToolingPreview = "androidx.compose.ui:ui-tooling-preview:${AndroidVersions.compose}"
            const val foundation = "androidx.compose.foundation:foundation:${AndroidVersions.compose}"
            const val activity = "androidx.activity:activity-compose:${AndroidVersions.activityCompose}"
            const val liveDataRuntime = "androidx.compose.runtime:runtime-livedata:${AndroidVersions.compose}"
            const val constraintLayout = "androidx.constraintlayout:constraintlayout-compose:${AndroidVersions.constraintLayout}"
        }
    }

    object Accompanist {
        const val insets = "com.google.accompanist:accompanist-insets:${AndroidVersions.accompanist}"
        const val insetsUi = "com.google.accompanist:accompanist-insets-ui:${AndroidVersions.accompanist}"
        const val systemUiController = "com.google.accompanist:accompanist-systemuicontroller:${AndroidVersions.accompanist}"
        const val pager = "com.google.accompanist:accompanist-pager:${AndroidVersions.accompanist}"
        const val pagerIndicator = "com.google.accompanist:accompanist-pager-indicators:${AndroidVersions.accompanist}"
        const val navigation ="com.google.accompanist:accompanist-navigation-material:${AndroidVersions.accompanist}"
        const val navigationAnimation = "com.google.accompanist:accompanist-navigation-animation:${AndroidVersions.accompanist}"
        const val flowLayout = "com.google.accompanist:accompanist-flowlayout:${AndroidVersions.accompanist}"
    }

    object Coroutines {
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${CommonVersions.coroutines}"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${CommonVersions.coroutines}"
    }

    object Coil {
        const val core = "io.coil-kt:coil:${AndroidVersions.coil}"
        const val compose = "io.coil-kt:coil-compose:${AndroidVersions.coil}"
        const val svg = "io.coil-kt:coil-svg:${AndroidVersions.coil}"
    }
}
