plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.kotlin.spring)
	alias(libs.plugins.spring.boot)
	alias(libs.plugins.spring.dependency.management)
	alias(libs.plugins.graalvm.native)
}

group = "com.github.msorkhpar.spring-ai"
version = "0.0.1-SNAPSHOT"
description = "Covering Udemy Spring-AI Fast Track Course - Section01"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(libs.versions.java.get().toInt())
	}
}

repositories {
	mavenCentral()
}


dependencies {
	implementation("com.github.msorkhpar.spring-ai:base:0.0.1-SNAPSHOT")

	implementation(libs.bundles.spring.web)
	implementation(libs.bundles.kotlin.essentials)
	implementation(libs.bundles.spring.ai.starters)

	developmentOnly(libs.bundles.dev.tools)

	testImplementation(libs.bundles.testing.common)
	testRuntimeOnly(libs.junit.platform.launcher)
}

dependencyManagement {
	imports {
		mavenBom(libs.spring.ai.bom.get().toString())
	}
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
	jvmArgs("-XX:+EnableDynamicAgentLoading")
	testLogging {
		events("passed", "skipped", "failed")
		exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
		showStandardStreams = false
	}
}

graalvmNative {
	binaries {
		named("main") {
			imageName.set("spring-ai-claude")
			mainClass.set("com.github.msorkhpar.ai.claude.SpringAiClaudeApplicationKt")
			buildArgs.add("--verbose")
			buildArgs.add("-H:+ReportExceptionStackTraces")
			buildArgs.add("-Ob")
			buildArgs.add("--enable-monitoring=jfr")

			buildArgs.add("-H:IncludeResources=.*\\.properties")
			buildArgs.add("-H:IncludeResources=.*\\.yaml")

		}
		named("test") {
			buildArgs.add("--verbose")
		}
	}
	metadataRepository {
		enabled.set(true)
	}
}
