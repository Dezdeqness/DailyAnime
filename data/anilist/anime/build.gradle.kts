plugins {
    alias(libs.plugins.com.dezdeqness.data)
}

android {
    namespace = "com.dezdeqness.data.anilist.anime"
}

dependencies {
    implementation(project(":contract:anime"))
    implementation(project(":contract:settings"))

    implementation(project(":data:core"))
    implementation(project(":data:anilist:remote"))

    // Unit Testing
    testImplementation(libs.junit.api)
    testImplementation(libs.junit.engine)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk.mockk)
    testImplementation(libs.androidx.test.junit)
}
