import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.jfrog.bintray.gradle.BintrayExtension
import org.gradle.api.publish.maven.MavenPom

val kotlinVersion = plugins.getPlugin(KotlinPluginWrapper::class.java).kotlinPluginVersion

project.group = "de.swirtz"
project.version = "0.0.9"
val artifactID = "ktsRunner"

plugins {
    kotlin("jvm") version "1.4.10"
    `maven-publish`
    `java-library`
    id("com.jfrog.bintray") version "1.8.4"
}


dependencies {
    implementation(kotlin("stdlib-jdk8", kotlinVersion))
    implementation(kotlin("reflect", kotlinVersion))
    implementation(kotlin("script-runtime", kotlinVersion))
    implementation(kotlin("script-util", kotlinVersion))
    implementation(kotlin("compiler-embeddable", kotlinVersion))
    implementation(kotlin("scripting-compiler-embeddable", kotlinVersion))
    implementation(kotlin("script-util", kotlinVersion))
    implementation("org.slf4j:slf4j-api:1.7.14")
    implementation("ch.qos.logback:logback-classic:1.1.3")
    implementation("net.java.dev.jna:jna:4.2.2")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.2")
    testImplementation("org.assertj:assertj-core:3.11.1")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:5.3.2")
}


repositories {
    mavenCentral()
    jcenter()
}

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.getByName("main").allSource)
}

val publicationName = "ktsRunner_pub"
publishing {
    publications {
        create<MavenPublication>(publicationName) {
            artifactId = artifactID
            from(components["java"])
            artifact(sourcesJar)
        }
    }
}

fun findProperty(s: String) = project.findProperty(s) as String?
bintray {
    user = findProperty("bintrayUser")
    key = findProperty("bintrayApiKey")
    publish = true
    setPublications(publicationName)
    pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
        repo = "KtsRunner"
        name = "KtsRunner"
        userOrg = "s1m0nw1"
        websiteUrl = "https://kotlinexpertise.com"
        githubRepo = "s1monw1/KtsRunner"
        vcsUrl = "https://github.com/s1monw1/KtsRunner"
        description = "Library for executing kts files from Kotlin"
        setLabels("kotlin")
        setLicenses("MIT")
        desc = description
    })
}


tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
    withType(Test::class.java) {
        testLogging.showStandardStreams = true
        useJUnitPlatform()
    }
    withType<GenerateMavenPom> {
        destination = file("$buildDir/libs/$artifactID.pom")
    }

}

