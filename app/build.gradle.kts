import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    alias(libs.plugins.com.dezdeqness.compose)
    alias(libs.plugins.com.dezdeqness.room)
    alias(libs.plugins.com.dezdeqness.firebase)
    alias(libs.plugins.com.dezdeqness.config)
    alias(libs.plugins.com.dezdeqness.detekt)
    alias(libs.plugins.screenshot)
    alias(libs.plugins.kotlin.serialization)
}

val appVersionName = version.toString()

println("Version name: $appVersionName")

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

kotlin {
    jvmToolchain(21)
}

android {
    defaultConfig {
        applicationId = "com.dezdeqness"
        versionCode = project.getParsedVersionCode(project.name)
        versionName = appVersionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            val props = Properties().apply {
                val file = if (rootProject.file("build.jenkins.properties").exists())
                    rootProject.file("build.jenkins.properties")
                else
                    rootProject.file("build.properties")

                load(file.inputStream())
            }

            keyAlias = props["keystore.key.alias"] as String
            keyPassword = props["keystore.key.password"] as String
            storeFile = file(props["keystore.release"] as String)
            storePassword = props["keystore.password"] as String
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }

        getByName("debug") {
            isMinifyEnabled = false
            isDebuggable = true
            applicationIdSuffix = ".debug"
            proguardFiles("proguard-rules.pro")
        }

        create("qa") {
            initWith(getByName("debug"))
            applicationIdSuffix = ".qa"
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        val javaVersion = JavaVersion.VERSION_21
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    namespace = "com.dezdeqness"

    experimentalProperties["android.experimental.enableScreenshotTest"] = true
}

dependencies {
    // Dagger
    implementation(libs.dagger.dagger)
    ksp(libs.dagger.compilier)

    // Network
    implementation(libs.square.okhttp)
    implementation(libs.square.logging)
    implementation(libs.square.retrofit)
    implementation(libs.square.moshi)
    implementation(libs.square.scalars)
    implementation(libs.apollo.runtime)

    implementation(libs.kotlinx.serialization)

    // Chucker
    debugImplementation(libs.chucker.library)
    releaseImplementation(libs.chucker.noop)
    add("qaImplementation", libs.chucker.library)

    // Coroutines
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.coroutinesAndroid)

    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.compose)

    // Material Design
    implementation(libs.google.material)

    // Core
    implementation(libs.androidx.core)

    // Appcompat
    implementation(libs.androidx.appcompat)

    // Fragment
    implementation(libs.androidx.fragment)

    // Collection
    implementation(libs.androidx.collection)

    // Unit Testing
    testImplementation(libs.junit.api)
    testImplementation(libs.junit.engine)
    testImplementation(libs.turbine)
    testImplementation(libs.kotlinx.coroutines.test)

    // Android Testing
    testImplementation(libs.androidx.test.junit)
    testImplementation(libs.androidx.test.espresso)

    // Compose
    implementation(libs.lottie.compose)
    implementation(libs.reorderable)

    // Mock
    testImplementation(libs.mockk.mockk)

    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil)
    implementation(libs.coil.gif)

    // Datastore
    implementation(libs.androidx.datastore.preferences)

    // Crypto
    implementation(libs.google.tink)
    implementation(libs.protobuf.javalite)

    // LeakCanary
    debugImplementation(libs.leak.canary)
    add("qaImplementation", libs.leak.canary)

    // Common
    implementation(project(":common:core-ui"))
    implementation(project(":common:core"))

    // Androidx
    implementation(libs.androidx.browser)

    implementation(libs.elmslie.core)

    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(project(":contract:auth"))
    implementation(project(":contract:history"))
    implementation(project(":contract:user"))

    implementation(project(":feature:achievements"))
    implementation(project(":feature:screenshotviewer"))

    screenshotTestImplementation(libs.androidx.compose.ui.tooling)
}

fun Project.getParsedVersionCode(projectName: String): Int {
    println("Project name: $projectName")
    val versionName = properties["version"].toString()
    val parts = versionName.split(".")
    println("Parts: $parts")

    if (parts.size != 3) {
        throw IllegalArgumentException("Version name must have three parts: major.minor.patch")
    }

    val major = parts[0].toInt()
    val minor = parts[1].toInt()
    val patch = parts[2].toInt()

    val incrementPostfixNumber = System.getenv("RELEASE_VERSION_CODE")?.toIntOrNull() ?: 999

    println("Version is $versionName")
    println("Major: $major")
    println("Minor: $minor")
    println("Patch: $patch")
    println("Postfix: $incrementPostfixNumber")

    val versionCode = major * 10000000 + minor * 100000 + patch * 1000 + incrementPostfixNumber
    println("Version code: $versionCode")

    return incrementPostfixNumber
}