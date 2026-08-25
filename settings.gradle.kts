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
include(":data:core")
include(":data:remote-common")

// domain
include(":domain:core")
include(":domain:anime")
include(":domain:userrate")
include(":domain:auth")
include(":domain:forum")
include(":domain:topic")
include(":domain:history")
include(":domain:favourite")

// common
include(":common:foundation-ui")
include(":common:foundation")
include(":common:foundation-test")
include(":common:architecture")

// contracts
include(":contract:auth")
include(":contract:history")
include(":contract:user")
include(":contract:anime")
include(":contract:settings")
include(":contract:favourite")
include(":contract:topic")
include(":contract:forum")
include(":contract:filter")
include(":contract:achievements")
include(":contract:calendar")
include(":contract:character")
include(":contract:home")
include(":contract:person")
include(":contract:userrate")

// features
include(":feature:achievements")
include(":feature:auth")
include(":feature:calendar")
include(":feature:screenshotviewer")
include(":feature:history")
include(":feature:home")
include(":feature:favourite")
include(":feature:onboarding")
include(":feature:personallist")
include(":feature:profile")
include(":feature:userrate")
include(":feature:search")
include(":feature:searchfilter")
include(":feature:settings")
include(":feature:stats")
include(":feature:topics")
include(":feature:topicdetails")
include(":feature:forum")
include(":feature:details:common")
include(":feature:details:anime")
include(":feature:details:character")
include(":feature:details:person")
include(":feature:details:related")

// shared
include(":shared:shared-presentation")
include(":shared:shared-domain")
