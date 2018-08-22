import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.ShadowApplicationPlugin
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.jfrog.bintray.gradle.BintrayExtension
import org.gradle.api.publish.maven.MavenPom

val kotlinVersion = plugins.getPlugin(KotlinPluginWrapper::class.java).kotlinPluginVersion

project.group = "de.swirtz"
project.version = "0.0.4"
val artifactID = "ktsRunner"

plugins {
    kotlin("jvm") version "1.2.61"
    `maven-publish`
    `java-library`
    id("com.jfrog.bintray") version "1.8.0"
    id("com.github.johnrengelman.shadow") version "2.0.2"
}

dependencies {
    implementation(kotlin("stdlib-jdk8", kotlinVersion))
    implementation(kotlin("reflect", kotlinVersion))
    implementation("org.slf4j:slf4j-api:1.7.14")
    implementation("ch.qos.logback:logback-classic:1.1.3")
    implementation(kotlin("script-runtime", kotlinVersion))
    implementation(kotlin("compiler-embeddable", kotlinVersion))
    implementation(kotlin("script-util", kotlinVersion))

    testCompile(kotlin("test-junit", kotlinVersion))
    testCompile("junit:junit:4.11")
}


repositories {
    mavenCentral()
    jcenter()
}

val shadowJar: ShadowJar by tasks
shadowJar.apply {
    baseName = artifactID
    classifier = null
}

fun MavenPom.addDependencies() = withXml {
    asNode().appendNode("dependencies").let { depNode ->
        configurations.compile.allDependencies.forEach {
            depNode.appendNode("dependency").apply {
                appendNode("groupId", it.group)
                appendNode("artifactId", it.name)
                appendNode("version", it.version)
            }
        }
    }
}

val publicationName = "${artifactID}_pub"
publishing {
    publications.invoke {
        publicationName(MavenPublication::class) {
            artifactId = artifactID
            artifact(shadowJar)
            pom.addDependencies()
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
    withType(GradleBuild::class.java) {
        dependsOn(shadowJar)
    }
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
    withType(Test::class.java) {
        testLogging.showStandardStreams = true
    }
    withType<GenerateMavenPom> {
        destination = file("$buildDir/libs/${shadowJar.archiveName}.pom")
    }
}

