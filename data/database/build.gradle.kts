plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    id("com.dezdeqness.config")
    id("com.dezdeqness.detekt")
    id("com.dezdeqness.room")
}

android {
    namespace = "com.dezdeqness.data.database"

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

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
    implementation(project(":contract:anime"))
    implementation(project(":contract:user"))
    implementation(project(":shared:shared-domain"))

    implementation(libs.square.moshi)

    implementation(libs.dagger.dagger)
    ksp(libs.dagger.compilier)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

kotlin {
    jvmToolchain(21)
}
