package com.dezdeqness.buildlogic

import com.dezdeqness.buildlogic.gradleplugins.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class FirebasePlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(target) {
            pluginManager.apply(libs.findPlugin("google-firebase-crashlytics").get().get().pluginId)

            dependencies {
                val bom = libs.findLibrary("google-firebase-bom").get()
                add("implementation", platform(bom))
                add("implementation", libs.findLibrary("google-firebase-analytics").get())
                add("implementation", libs.findLibrary("google-firebase-crashlytics").get())
                add("implementation", libs.findLibrary("google-firebase-config").get())
            }
        }
    }
}
