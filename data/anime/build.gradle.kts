plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    id("com.dezdeqness.config")
    id("com.dezdeqness.detekt")
}

android {
    namespace = "com.dezdeqness.data.anime"

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildTypes {
        create("qa") {}
    }
}

dependencies {
    implementation(project(":contract:anime"))
    implementation(project(":contract:settings"))

    implementation(project(":data:core"))

    implementation(libs.square.retrofit)

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
