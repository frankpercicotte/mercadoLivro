import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.0"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "2.1.20"
	kotlin("plugin.spring") version "2.1.20"
	kotlin("plugin.jpa") version "2.1.20"
}

group = "com.mercadolivro"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	implementation("org.flywaydb:flyway-core:7.7.0")

	implementation("org.springdoc:springdoc-openapi-ui:1.6.14")
//	implementation("io.springfox:springfox-swagger2:2.9.2")
//	implementation("io.springfox:springfox-swagger-ui:2.9.2")

	runtimeOnly("mysql:mysql-connector-java")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

kotlin {
	compilerOptions {
		jvmTarget.set(JvmTarget.JVM_17)
		javaParameters.set(true)
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
