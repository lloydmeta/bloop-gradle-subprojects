dependencies {
  testImplementation(project(":a", "tests"))
  testImplementation("org.scalatest:scalatest_2.12:3.1.2")
  testImplementation("org.scalatestplus:junit-4-12_2.12:3.1.2.0")
}

configurations.create("tests") {
  extendsFrom(configurations.testRuntime.get())
}
tasks.register<Jar>("testJar") {
  archiveClassifier.set("tests")
  from(sourceSets.test.get().output)
}
artifacts {
  add("tests", tasks.named("testJar"))
}