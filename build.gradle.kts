val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val ktor_rsocket_version: String by project

plugins {
    kotlin("jvm") version "1.8.10"
    id("io.ktor.plugin") version "2.2.4"
}

group = "com.example"
version = "0.0.1"

repositories {
    mavenCentral()
    maven {
        url = uri("https://kotlin.bintray.com/ktor")
    }
}

application {
    mainClass.set("com.example.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.rsocket.kotlin:rsocket-core:$ktor_rsocket_version")
    implementation("io.rsocket.kotlin:rsocket-ktor-server:$ktor_rsocket_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

tasks {
    create("stage").dependsOn("installDist")
}