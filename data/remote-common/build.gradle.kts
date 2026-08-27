plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.apollo)
    id("com.dezdeqness.config")
    id("com.dezdeqness.detekt")
}

android {
    namespace = "com.dezdeqness.data.remotecommon"

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildTypes {
        create("qa") {}
    }
}

dependencies {
    api(libs.apollo.runtime)

    implementation(libs.square.moshi)

    implementation(libs.dagger.dagger)
    ksp(libs.dagger.compilier)

    implementation(project(":contract:anime"))
    implementation(project(":contract:character"))
    implementation(project(":contract:filter"))
    implementation(project(":contract:home"))
    implementation(project(":contract:person"))
    implementation(project(":contract:user"))
    implementation(project(":contract:userrate"))
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
