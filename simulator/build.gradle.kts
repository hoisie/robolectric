plugins {
  alias(libs.plugins.robolectric.deployed.java.module)
  alias(libs.plugins.robolectric.java.module)
}

dependencies {
  annotationProcessor(libs.auto.service)
  annotationProcessor(libs.error.prone.core)

  api(project(":annotations"))
  api(project(":robolectric"))
  api(project(":utils"))
  api(project(":shadowapi"))
  api(project(":utils:reflector"))
  api(project(":sandbox"))
  api(project(":shadows:framework"))
  compileOnly(AndroidSdk.MAX_SDK.coordinates)
  compileOnly(libs.auto.service.annotations)
  api(libs.javax.annotation.api)
  api(libs.javax.inject)

  api(libs.asm)
  api(libs.asm.commons)
  api(libs.guava)
  compileOnly(libs.findbugs.jsr305)
}
