plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.arturbosch.detekt)
    alias(libs.plugins.com.dezdeqness.config)
}

android {
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    namespace = "com.dezdeqness.domain"

    buildTypes {
        create("qa") {}
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

dependencies {
    // Coroutines
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.coroutinesAndroid)

    implementation(project(":contract:auth"))
    implementation(project(":contract:user"))
    implementation(project(":contract:anime"))
    implementation(project(":contract:history"))

}
