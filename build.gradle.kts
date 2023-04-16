import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    application
    id("org.openjfx.javafxplugin") version "0.0.8"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.apache.commons:commons-csv:1.5")
    implementation("mysql:mysql-connector-java:8.0.25")
    implementation ("no.tornado:tornadofx:1.7.20")

    implementation ("io.insert-koin:koin-core:3.3.3")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClass.set("MainKt")
}

javafx {
    version = "11.0.2"
    modules("javafx.controls", "javafx.fxml")
}