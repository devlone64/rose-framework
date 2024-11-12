@file:Suppress("SpellCheckingInspection")

import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    java
    `java-library`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "dev.lone64.roseframework.plugin"
version = "1.0.0"

dependencies {
    implementation(project(":modules:Spigot"))

    compileOnly("org.spigotmc", "spigot-api", "1.20.4-R0.1-SNAPSHOT")

    compileOnly("org.projectlombok", "lombok", "1.18.32")
    annotationProcessor("org.projectlombok", "lombok", "1.18.32")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    java.sourceCompatibility = JavaVersion.VERSION_17
    java.targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<Javadoc> {
    (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
    (options as StandardJavadocDocletOptions).addStringOption("encoding", "UTF-8")
    (options as StandardJavadocDocletOptions).addStringOption("charSet", "UTF-8")
}

tasks.shadowJar {
    archiveFileName.set("RoseLib-${version}.jar")
}

tasks.withType<ProcessResources> {
    from("src/main/resources") {
        include("plugin.yml")
        filter<ReplaceTokens>("tokens" to mapOf("version" to project.version))
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
}