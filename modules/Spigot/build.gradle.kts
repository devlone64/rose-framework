@file:Suppress("SpellCheckingInspection")

plugins {
    java
    `java-library`
    `maven-publish`
}

group = "dev.lone64.roseframework.spigot"
version = "1.0.0"

dependencies {
    compileOnly("org.atteo.classindex", "classindex", "3.13")
    compileOnly("com.github.cryptomorin", "XSeries", "11.3.0")
    compileOnly("com.github.kevinsawicki", "http-request", "6.0")
    compileOnly("org.spongepowered", "configurate-yaml", "4.1.2")

    compileOnly("com.github.MilkBowl", "VaultAPI", "1.7")
    compileOnly("com.github.LoneDev6", "API-ItemsAdder", "3.6.1")

    compileOnly("net.kyori", "adventure-api", "4.13.0")
    compileOnly("net.kyori", "adventure-text-serializer-legacy", "4.13.0")

    compileOnly("com.mojang", "authlib", "6.0.54")
    compileOnly("commons-io", "commons-io", "2.17.0")
    compileOnly("org.projectlombok", "lombok", "1.18.32")
    compileOnly("org.jetbrains", "annotations", "20.1.0")
    compileOnly("org.apache.commons", "commons-lang3", "3.17.0")
    compileOnly("org.spigotmc", "spigot-api", "1.20.4-R0.1-SNAPSHOT")

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

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "dev.lone64"
            artifactId = "rose-framework-spigot"
            from(components["java"])
        }
    }

    repositories {
        maven {
            url = uri("https://repo.repsy.io/mvn/lone64/public")
            credentials {
                username = "${properties["maven.username"]}"
                password = "${properties["maven.password"]}"
            }
        }
    }
}