import java.io.FileInputStream
import java.util.Properties

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
include(":contract:favourite")

// features
include(":feature:achievements")
include(":feature:screenshotviewer")
include(":feature:history")
