package org.robolectric.simulator

import com.android.build.gradle.AppExtension
import java.io.File
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.testing.Test

class SimulatorPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    val androidExtension = getAndroidExtension(project)
    if (androidExtension == null) {
      return
    }

    project.afterEvaluate {
      project.tasks.register("simulate", JavaExec::class.java) {
        group = "simulation"
        description = "Runs the Robolectric simulator for the variant"
        configureTask(project, this)
      }
    }
  }

  private fun getAndroidExtension(project: Project) =
    project.extensions.findByType(AppExtension::class.java)

  private fun configureTask(project: Project, task: JavaExec) {

    val testTaskName = "testDebugUnitTest"
    val testTask = project.tasks.findByName(testTaskName) as? Test
    val buildDir = project.buildDir
    val resourceApkFile =
      File(buildDir, "intermediates/apk_for_local_test/debugUnitTest/apk-for-local-test.ap_")

    if (testTask == null) {
      throw IllegalStateException("Missing testDebugUntTest task")
    }

    val simulator =
      project.configurations
        .detachedConfiguration(
          project.dependencies.create("org.robolectric:simulator:4.15-SNAPSHOT")
        )
        .resolve()

    task.apply {
      classpath = testTask.classpath + project.files(simulator)
      jvmArgs =
        listOf(
          "--add-opens",
          "java.desktop/sun.awt=ALL-UNNAMED",
          "--add-opens",
          "java.base/sun.reflect.misc=ALL-UNNAMED",
          "--add-opens",
          "java.base/sun.security.action=ALL-UNNAMED",
          "--add-opens",
          "java.desktop/sun.swing=ALL-UNNAMED",
          "--add-opens",
          "java.desktop/sun.lwawt.macosx=ALL-UNNAMED",
        ) + testTask.jvmArgs
      mainClass.set("org.robolectric.simulator.SimulatorMain")
      args = listOf(resourceApkFile.absolutePath)
      standardOutput = System.out
      errorOutput = System.err
    }
  }
}
