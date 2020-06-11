dependencies {
    testImplementation("org.scalatest:scalatest_2.12:3.1.2")
    testImplementation("org.scalatestplus:junit-4-12_2.12:3.1.2.0")
}

tasks.register<Jar>("testJar") {
    archiveClassifier.set("tests")
    from(sourceSets.test.get().output)
}
configurations.create("tests") {
    extendsFrom(configurations.testRuntime.get())
}
artifacts {
    add("tests", tasks.named("testJar"))
}