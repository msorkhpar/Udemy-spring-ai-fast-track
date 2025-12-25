plugins {
	kotlin("jvm") version "2.2.21"
	kotlin("plugin.spring") version "2.2.21"
	id("org.springframework.boot") version "3.5.9"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.graalvm.buildtools.native") version "0.10.4"
}

group = "com.github.msorkhpar.spring-ai"
version = "0.0.1-SNAPSHOT"
description = "Covering Udemy Spring-AI Fast Track Course"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(24)
	}
}

repositories {
	mavenCentral()
}

extra["springAiVersion"] = "1.1.2"

dependencies {
	implementation("com.github.msorkhpar.spring-ai:base:0.0.1-SNAPSHOT")

	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.ai:spring-ai-starter-model-anthropic")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.ai:spring-ai-bom:${property("springAiVersion")}")
	}
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

graalvmNative {
	binaries {
		named("main") {
			imageName.set("spring-ai-claude")
			mainClass.set("com.github.msorkhpar.ai.claude.SpringAiClaudeApplicationKt")
			buildArgs.add("--verbose")
			buildArgs.add("-H:+ReportExceptionStackTraces")
			// Optimize for faster startup
			buildArgs.add("-Ob")
			// Enable monitoring with JFR
			buildArgs.add("--enable-monitoring=jfr")
		}
		named("test") {
			buildArgs.add("--verbose")
		}
	}
	metadataRepository {
		enabled.set(true)
	}
}
