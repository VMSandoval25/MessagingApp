pluginManagement {
    repositories {
        mavenCentral()
        maven { setUrl("https://plugins.gradle.org/m2/") }
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
dependencyResolutionManagement {
    repositories {
        google()
    }
}
rootProject.name = "sizzle_messaging"

include(":shared")
include(":web")
include(":android-app")
