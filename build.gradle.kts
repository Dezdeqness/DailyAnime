import org.jacoco.core.analysis.Analyzer
import org.jacoco.core.analysis.CoverageBuilder
import org.jacoco.core.tools.ExecFileLoader
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath(libs.gradle)
        classpath(libs.google.services)
        classpath("org.jacoco:org.jacoco.core:0.8.14")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.google.firebase.crashlytics) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.arturbosch.detekt)
    alias(libs.plugins.ksp) apply true
    id("jacoco")
}

jacoco {
    toolVersion = "0.8.14"
}

detekt {
    buildUponDefaultConfig = true
    parallel = true
    allRules = false
}

tasks.register<JacocoReport>("jacocoRootReport") {
    val modules = subprojects.filter {
        it.plugins.hasPlugin("com.android.library") ||
                it.plugins.hasPlugin("com.android.application")
    }

    dependsOn(modules.map { "${it.path}:testDebugUnitTest" })

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    println("Affected modules: ${modules.joinToString { it.displayName }}")

    val fileFilter = listOf(
        "**/*Test*.*",
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*\$inlined\$*.*",
        "**/*\$Lambda$*.*",
        "**/*\$Suspend*.*",
    )

    val classDirs = files(
        modules.map {
            fileTree("${it.buildDir}/intermediates/javac/debug/classes") {
                exclude(fileFilter)
            }
            fileTree("${it.buildDir}/tmp/kotlin-classes/debug") {
                exclude(fileFilter)
            }
        }
    )

    val sourceDirs = files(modules.map { "${it.projectDir}/src/main/java" })
    val execData = files(
        modules.map {
            it.fileTree("${it.buildDir}") {
                include(
                    "jacoco/testDebugUnitTest.exec",
                    "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.ec"
                )
            }
        }
    )

    classDirectories.setFrom(classDirs)
    sourceDirectories.setFrom(sourceDirs)
    executionData.setFrom(execData)

    doLast {
        val loader = ExecFileLoader()
        execData.files.forEach { loader.load(it) }

        val coverageBuilder = CoverageBuilder()
        val analyzer = Analyzer(loader.executionDataStore, coverageBuilder)
        classDirs.files.forEach { dir ->
            if (dir.exists()) analyzer.analyzeAll(dir)
        }

        var totalInstructions = 0
        var coveredInstructions = 0
        var totalClasses = 0
        var coveredClasses = 0

        coverageBuilder.classes.forEach { cls ->
            totalInstructions += cls.instructionCounter.totalCount
            coveredInstructions += cls.instructionCounter.coveredCount
            totalClasses += 1
            if (cls.instructionCounter.coveredCount > 0) {
                coveredClasses += 1
            }
        }

        val coveragePercent = if (totalInstructions > 0) {
            coveredInstructions * 100.0 / totalInstructions
        } else 0.0

        println("Overall coverage: %.2f%%".format(coveragePercent))
        println("Class covered: $coveredClasses out $totalClasses")
    }
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
