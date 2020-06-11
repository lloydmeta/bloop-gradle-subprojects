buildscript {
    repositories {
        // Add here whatever repositories you're already using
        mavenCentral()
        maven("https://repo.gradle.org/gradle/libs-releases-local/")
    }

    dependencies {
        classpath("ch.epfl.scala:gradle-bloop_2.12:1.4.1")
    }
}


subprojects {
    apply(plugin = "scala")
    apply(plugin = "bloop")

    repositories {
        mavenCentral()
    }

    dependencies {
        "implementation"("org.scala-lang:scala-library:2.12.11")
    }

}