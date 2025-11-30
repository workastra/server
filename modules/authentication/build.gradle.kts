plugins {
  `java-library`
  alias(libs.plugins.spring.dependency.management)
}

dependencies {
  implementation(platform(libs.spring.boot.bom))
  implementation(project(":modules:common"))
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.boot:spring-boot-starter-security-oauth2-client")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.springframework.boot:spring-boot-starter-security-oauth2-client-test")
  testImplementation("org.springframework.boot:spring-boot-starter-security-test")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
  useJUnitPlatform()
}
