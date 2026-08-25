import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    id("com.dezdeqness.config")
    id("com.dezdeqness.detekt")
}

fun properties(fileName: String) = Properties().apply {
    val file = rootProject.file(fileName)
    if (file.exists()) {
        file.inputStream().use { load(it) }
    }
}

val localProperties = properties("local.properties")
val buildProperties = properties("build.properties")

fun secret(key: String): String =
    System.getenv(key)
        ?: localProperties.getProperty(key)
        ?: buildProperties.getProperty(key)
        ?: ""

android {
    defaultConfig {
        buildConfigField("String", "SHIKIMORI_CLIENT_ID", "\"${secret("SHIKIMORI_CLIENT_ID")}\"")
        buildConfigField("String", "SHIKIMORI_CLIENT_SECRET", "\"${secret("SHIKIMORI_CLIENT_SECRET")}\"")
        buildConfigField("String", "SHIKIMORI_REDIRECT_URI", "\"${secret("SHIKIMORI_REDIRECT_URI")}\"")
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    namespace = "com.dezdeqness.data.core"

    buildTypes {
        create("qa") {}
    }
}

dependencies {
    api(project(":domain:core"))
    api(project(":data:remote-common"))
    implementation(project(":contract:anime"))
    implementation(project(":contract:filter"))
    implementation(project(":contract:settings"))

    // Datastore
    implementation(libs.androidx.datastore.preferences)

    // Coroutines
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.coroutinesAndroid)

    // Network
    implementation(libs.square.retrofit)

    // Moshi
    implementation(libs.square.moshi)

    // Dagger
    implementation(libs.dagger.dagger)
    ksp(libs.dagger.compilier)

    // Firebase
    implementation(libs.google.firebase.crashlytics)
    implementation(libs.google.firebase.config)

    implementation(project(":common:foundation"))
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

kotlin {
    jvmToolchain(21)
}
