package com.dezdeqness.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import com.dezdeqness.buildlogic.gradleplugins.commonExtension
import com.dezdeqness.buildlogic.gradleplugins.configureAndroidCompose
import com.dezdeqness.buildlogic.gradleplugins.libs

class AndroidComposePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(libs.findPlugin("compose-compiler").get().get().pluginId)

            commonExtension.apply {
                configureAndroidCompose(this)
            }
        }
    }
}
