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
        maven {
            url = uri("https://maven.pkg.github.com/Dezdeqness/Android-Support-Things")
            credentials {
                username = props["github.username"].toString()
                password = props["github.token"].toString()
            }
        }
    }
}

rootProject.name = "Daily Anime"

include(":app")
include(":advanced_recycler")
include(":data")
include(":domain")