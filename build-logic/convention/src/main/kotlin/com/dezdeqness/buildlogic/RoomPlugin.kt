package com.dezdeqness.buildlogic

import com.dezdeqness.buildlogic.gradleplugins.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class RoomPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        dependencies {
            add("implementation", libs.findLibrary("androidx-room-runtime").get())
            add("ksp", libs.findLibrary("androidx-room-compiler").get())
        }
    }
}
