import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.arturbosch.detekt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.apollo)
    id("com.google.protobuf") version "0.9.4"
    alias(libs.plugins.com.dezdeqness.config)
    alias(libs.plugins.com.dezdeqness.room)
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
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "SHIKIMORI_CLIENT_ID", "\"${secret("SHIKIMORI_CLIENT_ID")}\"")
        buildConfigField("String", "SHIKIMORI_CLIENT_SECRET", "\"${secret("SHIKIMORI_CLIENT_SECRET")}\"")
        buildConfigField("String", "SHIKIMORI_REDIRECT_URI", "\"${secret("SHIKIMORI_REDIRECT_URI")}\"")
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    namespace = "com.dezdeqness.data"

    buildTypes {
        create("qa") {}
    }
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.incremental", "true")
    arg("room.expandProjection", "true")
}

dependencies {
    implementation(project(":domain"))

    // Crypto
    implementation(libs.google.tink)
    implementation(libs.protobuf.javalite)

    // Datastore
    implementation(libs.androidx.datastore.preferences)

    // Coroutines
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.coroutinesAndroid)

    // Network
    implementation(libs.square.okhttp)
    implementation(libs.square.logging)
    implementation(libs.square.retrofit)
    implementation(libs.square.moshi)
    implementation(libs.square.scalars)
    implementation(libs.apollo.runtime)

    // Dagger
    implementation(libs.dagger.dagger)
    ksp(libs.dagger.compilier)

    // Core
    implementation(libs.androidx.core)

    // Appcompat
    implementation(libs.androidx.appcompat)

    // Firebase
    implementation(libs.google.firebase.analytics)
    implementation(libs.google.firebase.crashlytics)
    implementation(libs.google.firebase.config)

    implementation(project(":contract:auth"))
    implementation(project(":contract:history"))
    implementation(project(":contract:user"))
    implementation(project(":contract:settings"))
    implementation(project(":contract:anime"))
    implementation(project(":contract:favourite"))
    implementation(project(":contract:topic"))
    implementation(project(":contract:forum"))

    implementation(project(":shared:shared-domain"))

    implementation(project(":common:foundation"))
}

apollo {
    service("service") {
        packageName.set("com.dezdeqness.data")
        schemaFile.set(file("src/main/graphql/schema.graphqls"))
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

kotlin {
    jvmToolchain(21)
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:4.30.1"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
            }
        }
    }
}
