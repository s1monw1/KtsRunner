import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

project.group = "de.swirtz"

plugins {
    kotlin("jvm") version "1.4.20"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("org.slf4j:slf4j-api:1.7.30")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("de.swirtz:ktsRunner:0.0.9")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    testImplementation("org.assertj:assertj-core:3.12.0")
}


repositories {
    mavenCentral()
    jcenter()
    maven {
        setUrl("https://dl.bintray.com/s1m0nw1/KtsRunner")
    }
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
    withType(Test::class.java) {
        testLogging.showStandardStreams = true
        useJUnitPlatform()
    }
}

