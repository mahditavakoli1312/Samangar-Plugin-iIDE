plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.23"
    id("org.jetbrains.intellij") version "1.17.3"
}

group = "ir.mahdi-tavakoli"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:20.1.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.google.code.gson:gson:2.9.1")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.github.eloyzone:jalali-calendar:1.0.0")
    implementation("org.jetbrains:annotations:20.1.0")

}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html


intellij {
    // Define IntelliJ Platform against which to build the plugin project.

    // Same IntelliJ IDEA version (2019.1.4) as target 3.5 Android Studio:
    version.set("2023.2.6")

    // Use IntelliJ IDEA CE because it's the basis of the IntelliJ Platform:
    type.set("IC")

    // Require the Android plugin (Gradle will choose the correct version):
    plugins.set(listOf("android"))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("232")
        untilBuild.set("242.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }

    runIde {
        // Absolute path to installed target 3.5 Android Studio to use as
        // IDE Development Instance (the "Contents" directory is macOS specific):
        ideDir.set(file("/home/mahditavakoli/.local/share/JetBrains/Toolbox/apps/android-studio"))
    }
}
