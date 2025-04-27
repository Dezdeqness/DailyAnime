import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.File

buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath(libs.gradle)
        classpath(libs.google.services)
        classpath(libs.androidx.navigation.safeargs)
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.google.firebase.crashlytics) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.arturbosch.detekt)
    alias(libs.plugins.ksp) apply false
}

detekt {
    buildUponDefaultConfig = true
    parallel = true
    allRules = false
}

tasks.named<io.gitlab.arturbosch.detekt.Detekt>("detekt").configure {
    reports {
        xml.required.set(true)
        xml.outputLocation.set(file("build/reports/detekt.xml"))

        html.required.set(true)
        html.outputLocation.set(file("build/reports/detekt.html"))

        txt.required.set(true)
        txt.outputLocation.set(file("build/reports/detekt.txt"))

        sarif.required.set(true)
        sarif.outputLocation.set(file("build/reports/detekt.sarif"))

        md.required.set(true)
        md.outputLocation.set(file("build/reports/detekt.md"))

        custom {
            reportId = "CustomJsonReport"
            outputLocation.set(file("build/reports/detekt.json"))
        }
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}

tasks.register<Copy>("copyGitHooks") {
    description = "Copies the git hooks from /scripts to .git/hooks."
    group = "git hooks"
    from("$rootDir/scripts/pre-commit")
    from("$rootDir/scripts/pre-push")
    into("$rootDir/.git/hooks/")
}

tasks.register<Exec>("installGitHooks") {
    description = "Installs the pre-check git hooks from /git-hooks."
    group = "git hooks"
    workingDir = rootDir
    if (!org.gradle.internal.os.OperatingSystem.current().isWindows) {
        commandLine("chmod", "-R", "+x", ".git/hooks/")
    } else {
        commandLine(
            "powershell",
            "-Command",
            "Get-ChildItem -Path .git/hooks/* -Recurse | ForEach-Object { \$_.Attributes = 'Normal' }"
        )
    }
    dependsOn("copyGitHooks")
    doLast {
        logger.info("Git hook installed successfully.")
    }
}


subprojects {
    afterEvaluate {
        tasks.findByName("preBuild")?.let { preBuildTask ->
            preBuildTask.dependsOn(rootProject.tasks.named("installGitHooks"))
        }
    }
}

tasks.named("clean") {
    dependsOn("installGitHooks")
}

subprojects {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            project.findProperty("composeCompilerReports")?.takeIf { it == "true" }?.let {
                freeCompilerArgs += listOf(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${project.buildDir.absolutePath}/compose_compiler"
                )
            }
            project.findProperty("composeCompilerMetrics")?.takeIf { it == "true" }?.let {
                freeCompilerArgs += listOf(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${project.buildDir.absolutePath}/compose_compiler"
                )
            }
        }
    }
}
