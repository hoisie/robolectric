plugins {
  `kotlin-dsl`
  `java-gradle-plugin`
  alias(libs.plugins.robolectric.deployed.java.module)
  alias(libs.plugins.robolectric.spotless)
}

gradlePlugin {
  plugins {
    create("simulatorPlugin") {
      id = "org.robolectric.simulator"
      implementationClass = "org.robolectric.simulator.SimulatorPlugin"
    }
  }
}

dependencies {
  compileOnly("com.android.tools.build:gradle:8.2.2")
  implementation(libs.kotlin.stdlib)
  implementation(project(":simulator"))
}
