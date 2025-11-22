plugins {
  java
  alias(libs.plugins.spring.boot) apply false
  alias(libs.plugins.spring.dependency.management) apply false
  alias(libs.plugins.graalvm.native) apply false
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

  java {
    toolchain {
      languageVersion.set(JavaLanguageVersion.of(25))
    }
  }
}
