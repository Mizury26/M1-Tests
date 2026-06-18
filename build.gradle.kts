plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.14"
    id("io.spring.dependency-management") version "1.1.7"
    id("io.kotest") version "6.1.11"
    id("jacoco")
    id("info.solidsoft.pitest") version "1.19.0-rc.2"
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "TP-test"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

testing {
    suites {

        val testIntegration by registering(JvmTestSuite::class) {
            sources {
                kotlin {
                    setSrcDirs(listOf("src/testIntegration/kotlin"))
                }
                compileClasspath += sourceSets.main.get().output
                runtimeClasspath += sourceSets.main.get().output
            }

        }

        val testComponent by registering(JvmTestSuite::class) {

            sources {
                kotlin {
                    setSrcDirs(listOf("src/testComponent/kotlin"))
                }
                compileClasspath += sourceSets.main.get().output
                runtimeClasspath += sourceSets.main.get().output
            }
        }
        val testArchitecture by registering(JvmTestSuite::class) {

            sources {
                kotlin {
                    setSrcDirs(listOf("src/testArchitecture/kotlin"))
                }
                compileClasspath += sourceSets.main.get().output
                runtimeClasspath += sourceSets.main.get().output
            }
        }
    }
}


val testIntegrationImplementation: Configuration by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

val testComponentImplementation: Configuration by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

val testArchitectureImplementation: Configuration by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.liquibase:liquibase-core")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.postgresql:postgresql")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("io.kotest:kotest-assertions-core:6.1.11")
    testImplementation("io.kotest:kotest-property:6.1.11")
    testImplementation("io.kotest:kotest-runner-junit5:6.1.11")
    testImplementation("io.mockk:mockk:1.14.9")
    testImplementation("org.junit.platform:junit-platform-suite")
    testIntegrationImplementation("org.testcontainers:postgresql:1.19.1")
    testIntegrationImplementation("org.testcontainers:jdbc-test:1.12.0")
    testIntegrationImplementation("org.testcontainers:testcontainers:1.19.1")
    testIntegrationImplementation("io.kotest.extensions:kotest-extensions-testcontainers:2.0.2")
    pitest("org.pitest:pitest-junit5-plugin:1.2.3")
    testIntegrationImplementation("io.mockk:mockk:1.14.9")
    testIntegrationImplementation("io.kotest:kotest-assertions-core:5.9.1")
    testIntegrationImplementation("io.kotest:kotest-runner-junit5:5.9.1")
    testIntegrationImplementation("com.ninja-squad:springmockk:4.0.2")
    testIntegrationImplementation("io.kotest.extensions:kotest-extensions-spring:1.3.0")
    testIntegrationImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "mockito-core")
    }
    testComponentImplementation("io.cucumber:cucumber-java:7.14.0")
    testComponentImplementation("io.cucumber:cucumber-spring:7.14.0")
    testComponentImplementation("io.cucumber:cucumber-junit:7.14.0")
    testComponentImplementation("io.cucumber:cucumber-junit-platform-engine:7.14.0")
    testComponentImplementation("io.rest-assured:rest-assured:5.3.2")
    testComponentImplementation("org.junit.platform:junit-platform-suite:1.10.0")
    testComponentImplementation("org.testcontainers:postgresql:1.19.1")
    testComponentImplementation("io.kotest:kotest-assertions-core:5.9.1")
    testComponentImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(kotlin("test"))
    testComponentImplementation(kotlin("test"))
    testArchitectureImplementation("com.tngtech.archunit:archunit-junit5:1.3.0")
    testArchitectureImplementation("io.kotest:kotest-assertions-core:5.9.1")
    testArchitectureImplementation("io.kotest:kotest-runner-junit5:5.9.1")
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.8")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}
tasks.named<Test>("testIntegration") {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.named<Test>("testComponent") {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.named<Test>("testArchitecture") {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    dependsOn(tasks.named<Test>("testIntegration"))
    dependsOn(tasks.named<Test>("testComponent"))
    dependsOn(tasks.named<Test>("testArchitecture"))


    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }
}

tasks.named<io.gitlab.arturbosch.detekt.Detekt>("detekt") {
    autoCorrect = true
}

pitest {
    junit5PluginVersion.set("1.2.3")
    pitestVersion.set("1.20.3")

    targetClasses.set(listOf("com.example.tptest.*"))
    targetTests.set(setOf("com.example.tptest.*"))

    outputFormats.set(listOf("HTML", "XML"))
    timestampedReports.set(false)
    verbose.set(true)
}

configurations.named("detekt") {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.jetbrains.kotlin") {
            useVersion("2.0.21")
        }
    }
}


