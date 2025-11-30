plugins {
  java
  checkstyle
  alias(libs.plugins.spring.boot) apply false
  alias(libs.plugins.spring.dependency.management) apply false
  alias(libs.plugins.graalvm.native) apply false
  alias(libs.plugins.spotless)
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
}
