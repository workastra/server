plugins {
  `java-library`
  alias(libs.plugins.spring.dependency.management)
}

dependencies {
  implementation(platform(libs.spring.boot.bom))
  implementation("org.springframework.boot:spring-boot-starter")
}