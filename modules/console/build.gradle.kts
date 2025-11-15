plugins {
  java
  alias(libs.plugins.spring.boot) 
  alias(libs.plugins.spring.dependency.management)
  alias(libs.plugins.graalvm.native) 
}

dependencies {
  implementation(project(":modules:common"))
  implementation("org.springframework.boot:spring-boot-starter-webmvc")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
  useJUnitPlatform()
}
