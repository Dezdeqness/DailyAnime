pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
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
include(":common:foundation-ui")
include(":common:foundation")
include(":common:foundation-test")

// contracts
include(":contract:auth")
include(":contract:history")
include(":contract:user")
include(":contract:anime")
include(":contract:settings")
include(":contract:favourite")
include(":contract:topic")
include(":contract:forum")

// features
include(":feature:achievements")
include(":feature:screenshotviewer")
include(":feature:history")
include(":feature:favourite")
include(":feature:onboarding")
include(":feature:personallist")
include(":feature:userrate")
include(":feature:settings")
include(":feature:news")
include(":feature:topicdetails")

// shared
include(":shared:shared-presentation")
include(":shared:shared-domain")
