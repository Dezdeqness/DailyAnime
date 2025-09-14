import java.util.Properties
import java.io.FileInputStream

pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

val props = Properties().apply {
    load(FileInputStream("local.properties"))
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Daily Anime"

include(":app")
include(":data")
include(":domain")

// common
include(":common:core-ui")
include(":common:core")

// contracts
include(":contract:auth")
include(":contract:history")
include(":contract:user")
include(":contract:anime")
include(":contract:settings")

// features
include(":feature:achievements")
include(":feature:screenshotviewer")
