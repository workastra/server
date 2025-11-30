plugins {
  java
  alias(libs.plugins.spring.boot)
  alias(libs.plugins.spring.dependency.management)
  alias(libs.plugins.graalvm.native)
}

configurations {
  compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
  }
}

dependencies {
  implementation(project(":modules:common"))
  implementation(project(":modules:authentication"))
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-webmvc")
  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
  testImplementation("org.springframework.boot:spring-boot-starter-actuator-test")
  testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
  useJUnitPlatform()
}

graalvmNative {
  binaries {
    named("main") {
      buildArgs.addAll(
        "-Ob",
        "--no-fallback",
        "--static-nolibc"
      )
    }

    named("test") {
      buildArgs.addAll(
        "-Ob",
        "--no-fallback",
        "--static-nolibc"
      )
    }
  }
}
