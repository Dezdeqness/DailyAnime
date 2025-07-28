package com.dezdeqness.buildlogic.gradleplugins

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion = libs.findVersion("kotlinCompilerExtensionVersion").get().toString()
        }
    }

    dependencies {
        val bom = libs.findLibrary("androidx-compose-bom").get()

        add("implementation", platform(bom))
        add("androidTestImplementation", platform(bom))

        add("implementation", libs.findLibrary("androidx-lifecycle-compose").get())
        add("implementation", libs.findLibrary("androidx-activity-compose").get())
        add("implementation", libs.findLibrary("androidx-compose-ui-ui").get())
        add("implementation", libs.findLibrary("androidx-compose-ui-util").get())
        add("implementation", libs.findLibrary("androidx-compose-ui-graphics").get())
        add("implementation", libs.findLibrary("androidx-compose-ui-preview").get())
        add("implementation", libs.findLibrary("androidx-compose-material3").get())
        add("implementation", libs.findLibrary("androidx-compose-material").get())
        add("implementation", libs.findLibrary("androidx-compose-icons").get())
        add("implementation", libs.findLibrary("androidx-compose-icons-extended").get())
        add("implementation", libs.findLibrary("androidx-compose-navigation").get())
        add("debugImplementation", libs.findLibrary("androidx-compose-ui-tooling").get())
    }
}
