// settings.gradle.kts for AegisNav
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // MapLibre repository (no authentication required)
        maven {
            url = uri("https://maven.maplibre.org/releases/")
        }
    }
}

rootProject.name = "AegisNav"
include(":app")
