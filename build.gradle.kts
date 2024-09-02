
val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val kotest_version: String by project

plugins {
    kotlin("jvm") version "2.0.10"
    kotlin("plugin.serialization") version "2.0.10"
    id("io.ktor.plugin") version "2.3.12"
}

group = "jp.co.upsider"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-config-yaml")
    testImplementation("io.ktor:ktor-server-test-host-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")


    // HikariCP
    implementation("com.zaxxer:HikariCP:5.1.0")

    // Exposed dependencies
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")

    // Database driver (for MySQL)
    implementation("mysql:mysql-connector-java:8.0.33")

    // Coroutines (if needed)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    //validation
    implementation("org.valiktor:valiktor-core:0.12.0")

    //言語拡張
    implementation("com.michael-bull.kotlin-result:kotlin-result:1.1.18")

    // 環境変数
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")

    // テスト
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("io.ktor:ktor-server-test-host-jvm:$ktor_version")

    testImplementation("io.kotest:kotest-runner-junit5:$kotest_version")
    testImplementation("io.kotest:kotest-assertions-core:$kotest_version")
    testImplementation("io.kotest:kotest-property:$kotest_version")
    testImplementation("io.kotest:kotest-framework-engine-jvm:$kotest_version")
    testImplementation("io.kotest.extensions:kotest-assertions-ktor:1.0.3")
    testImplementation("io.kotest.extensions:kotest-extensions-testcontainers:1.3.4")
    testImplementation("io.kotest.extensions:kotest-extensions-koin:1.1.0")
    testImplementation("org.testcontainers:testcontainers:1.19.7")
    testImplementation("org.testcontainers:mysql:1.19.6")
    testImplementation("org.testcontainers:localstack:1.19.4")
    testImplementation("com.h2database:h2:2.1.214")
}


tasks.withType<Test> {
    useJUnitPlatform()
}
