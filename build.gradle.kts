import net.ltgt.gradle.errorprone.errorprone

val libsCatalog = extensions
  .getByType(VersionCatalogsExtension::class.java)
  .named("libs")

plugins {
  java
  checkstyle
  alias(libs.plugins.spring.boot) apply false
  alias(libs.plugins.spring.dependency.management) apply false
  alias(libs.plugins.graalvm.native) apply false
  alias(libs.plugins.spotless)
  alias(libs.plugins.net.ltgt.errorprone) apply true
}

group = "com.workastra"
version = "0.0.1-SNAPSHOT"
description = "Workastra Server"

allprojects {
  group = "com.workastra"
  version = "0.0.1-SNAPSHOT"

  repositories {
    mavenCentral()
  }
}

subprojects {
  apply(plugin = "java")
  apply(plugin = "checkstyle")
  apply(plugin = "com.diffplug.spotless")
  apply(plugin = "net.ltgt.errorprone")

  checkstyle {
    toolVersion = "12.1.2"
    configFile = file("${rootDir}/config/checkstyle/checkstyle.xml")
  }

  java {
    toolchain {
      languageVersion.set(JavaLanguageVersion.of(25))
    }
  }

  spotless {
    java {
      target("src/*/java/**/*.java")

      prettier(mapOf(
        "prettier" to "3.7.3",
        "prettier-plugin-java" to "2.7.7"
      )).configFile(file("${rootDir}/java.prettierrc.yaml"))
    }
  }

  dependencies {
    add("errorprone", libsCatalog.findLibrary("error-prone-core").get())
    add("errorprone", libsCatalog.findLibrary("nullaway").get())
  }

  tasks.withType<JavaCompile>().configureEach {
    options.errorprone {
      disableWarningsInGeneratedCode.set(true)
      excludedPaths.set(".*/build/generated/.*")

      // Enable nullness checks only in null-marked code
      option("NullAway:OnlyNullMarked", "true")
      // Bump checks from warnings (default) to errors
      error("NullAway")
      // https://github.com/uber/NullAway/wiki/JSpecify-Support
      option("NullAway:JSpecifyMode", "true")
    }
  }
}
