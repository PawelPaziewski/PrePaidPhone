plugins {
    id("org.springframework.boot") version "2.6.1"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.0"
}

group = "pl.kul"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.axonframework:axon-spring-boot-starter:4.5.9")
    implementation("org.springframework.boot:spring-boot-starter-web:2.6.7")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:2.6.7")
    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect:1.6.10")

    testImplementation("org.axonframework:axon-test:4.5.9")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.6.7")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}