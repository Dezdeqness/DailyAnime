plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("com.dezdeqness.config")
    id("com.dezdeqness.detekt")
}

android {
    namespace = "com.dezdeqness.data.analytics"

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildTypes {
        create("qa") {}
    }
}

dependencies {
    implementation(project(":contract:user"))

    implementation(libs.google.firebase.analytics)
    implementation(libs.androidx.core)

    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.coroutinesAndroid)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

kotlin {
    jvmToolchain(21)
}
