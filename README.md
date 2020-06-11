## Bloop with Gradle

This is a minimal recreation of an issue I encountered with Bloop and Gradle with subprojects, in
particular:

- Project 'a' has class in its `test` configuration
- Project 'b' depends on `a`'s `test` and uses it

Issue in `bloop` repo https://github.com/scalacenter/bloop/issues/1311

### The Gradle project works fine

```shell
❯ ./gradlew b:test

BUILD SUCCESSFUL in 6s
6 actionable tasks: 6 executed
```

### The `bloopInstall` task finishes

```shell script
❯ ./gradlew bloopInstall

Deprecated Gradle features were used in this build, making it incompatible with Gradle 7.0.
Use '--warning-mode all' to show the individual deprecation warnings.
See https://docs.gradle.org/6.5/userguide/command_line_interface.html#sec:command_line_warnings

BUILD SUCCESSFUL in 493ms
4 actionable tasks: 4 executed

```

### bloop compile works for non-test configs

```shell
❯ bloop compile a
Compiling a (1 Scala source)
Compiled a (50ms)
❯ bloop compile b
Compiling b (1 Scala source)
Compiled b (50ms)
```

### bloop test works for project `a`

```shell
❯ bloop test a
Compiling a-test (1 Scala source)
Compiled a-test (124ms)
ASpec:
- should w/e
Execution took 15ms
1 tests, 1 passed
All tests in com.beachape.ASpec passed
```

### bloop test dies for project `b`

```shell
❯ bloop test b
Compiling b-test (1 Scala source)
[E] [E4] b/src/test/scala/com/beachape/BSpec.scala:9:3
[E]      not found: value it
[E]      L9:   it("should lol") {
[E]            ^
[E] [E3] b/src/test/scala/com/beachape/BSpec.scala:6:12
[E]      not found: value yo
[E]      L6:     assert(yo == "yo")
[E]                     ^
[E] [E2] b/src/test/scala/com/beachape/BSpec.scala:5:3
[E]      not found: value it
[E]      L5:   it("should yo") {
[E]            ^
[E] [E1] b/src/test/scala/com/beachape/BSpec.scala:3:21
[E]      not found: type ASpecLike
[E]      L3: class BSpec extends ASpecLike {
[E]                              ^
[E] b/src/test/scala/com/beachape/BSpec.scala: L3 [E1], L5 [E2], L6 [E3], L9 [E4]
Compiled b-test (45ms)
[E] Failed to compile 'b-test'
```

### Looking at `.bloop/b-test.json`

We see that there are no dependencies declared from `b-test.json` to `a-test`

<details>

```json
{
    "version": "1.4.0",
    "project": {
        "name": "b-test",
        "directory": "/Users/lloyd/Documents/skala/bloop-on-gradle-with-subprojects/b",
        "workspaceDir": "/Users/lloyd/Documents/skala/bloop-on-gradle-with-subprojects",
        "sources": [
            "/Users/lloyd/Documents/skala/bloop-on-gradle-with-subprojects/b/src/test/scala",
            "/Users/lloyd/Documents/skala/bloop-on-gradle-with-subprojects/b/src/test/java"
        ],
        "dependencies": [
            "b",
            "a"
        ],
        "classpath": [
            "/Users/lloyd/Documents/skala/bloop-on-gradle-with-subprojects/.bloop/b/build/classes",
            "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scalatestplus/junit-4-12_2.12/3.1.2.0/2743ef3c3b767525f9b01a1ccf4417858ed63cd6/junit-4-12_2.12-3.1.2.0.jar",
            "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scalatest/scalatest_2.12/3.1.2/3eed6b0c7e1eb8840df24d3e1cd6b93baf2458c7/scalatest_2.12-3.1.2.jar",
            "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scalactic/scalactic_2.12/3.1.2/7b1721e9bd243b280510920526688ceba75d5138/scalactic_2.12-3.1.2.jar",
            "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-lang/scala-reflect/2.12.11/7695010d1f4309a9c4b65be33528e382869ab3c4/scala-reflect-2.12.11.jar",
            "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-lang.modules/scala-xml_2.12/1.2.0/5d38ac30beb8420dd395c0af447ba412158965e6/scala-xml_2.12-1.2.0.jar",
            "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-lang/scala-library/2.12.11/1a0634714a956c1aae9abefc83acaf6d4eabfa7d/scala-library-2.12.11.jar",
            "/Users/lloyd/Documents/skala/bloop-on-gradle-with-subprojects/.bloop/a/build/classes",
            "/Users/lloyd/.gradle/caches/modules-2/files-2.1/junit/junit/4.12/2973d150c0dc1fefe998f834810d68f278ea58ec/junit-4.12.jar",
            "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.hamcrest/hamcrest-core/1.3/42a25dc3219429f0e5d060061f71acb49bf010a0/hamcrest-core-1.3.jar"
        ],
        "out": "/Users/lloyd/Documents/skala/bloop-on-gradle-with-subprojects/.bloop/b-test/build",
        "classesDir": "/Users/lloyd/Documents/skala/bloop-on-gradle-with-subprojects/.bloop/b-test/build/classes",
        "resources": [
            "/Users/lloyd/Documents/skala/bloop-on-gradle-with-subprojects/b/src/test/resources"
        ],
        "scala": {
            "organization": "org.scala-lang",
            "name": "scala-compiler",
            "version": "2.12.11",
            "options": [
                "-deprecation",
                "-unchecked"
            ],
            "jars": [
                "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-lang/scala-compiler/2.12.11/b93a7407c66f94ebe76c25a1fef17b0b8ecdaf1/scala-compiler-2.12.11.jar",
                "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-sbt/compiler-bridge_2.12/1.3.5/7970db798162352a0424936b1b1680b7478ecd5d/compiler-bridge_2.12-1.3.5-sources.jar",
                "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-sbt/compiler-interface/1.3.5/1f7379ff56b1795b523838cd06ff24272104c380/compiler-interface-1.3.5.jar",
                "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-lang/scala-reflect/2.12.11/7695010d1f4309a9c4b65be33528e382869ab3c4/scala-reflect-2.12.11.jar",
                "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-lang/scala-library/2.12.11/1a0634714a956c1aae9abefc83acaf6d4eabfa7d/scala-library-2.12.11.jar",
                "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-lang.modules/scala-xml_2.12/1.0.6/e22de3366a698a9f744106fb6dda4335838cf6a7/scala-xml_2.12-1.0.6.jar",
                "/Users/lloyd/.gradle/caches/modules-2/files-2.1/com.google.protobuf/protobuf-java/3.7.0/dbb5e9230a91f2a6d011096c2b9c10a5a6e5f7f2/protobuf-java-3.7.0.jar",
                "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-sbt/util-interface/1.3.0/edc7556e112da142bf7e9ead1440d024fd3314c4/util-interface-1.3.0.jar"
            ],
            "setup": {
                "order": "java->scala",
                "addLibraryToBootClasspath": true,
                "addCompilerToClasspath": false,
                "addExtraJarsToClasspath": false,
                "manageBootClasspath": true,
                "filterLibraryFromClasspath": true
            }
        },
        "java": {
            "options": [
                "-source",
                "1.8",
                "-target",
                "1.8",
                "-h",
                "/Users/lloyd/Documents/skala/bloop-on-gradle-with-subprojects/b/build/generated/sources/headers/java/test",
                "-g",
                "-sourcepath",
                "-proc:none",
                "-XDuseUnsharedTable=true"
            ]
        },
        "test": {
            "frameworks": [
                {
                    "names": [
                        "com.novocode.junit.JUnitFramework"
                    ]
                },
                {
                    "names": [
                        "org.scalatest.tools.Framework",
                        "org.scalatest.tools.ScalaTestFramework"
                    ]
                },
                {
                    "names": [
                        "org.scalacheck.ScalaCheckFramework"
                    ]
                },
                {
                    "names": [
                        "org.specs.runner.SpecsFramework",
                        "org.specs2.runner.Specs2Framework",
                        "org.specs2.runner.SpecsFramework"
                    ]
                },
                {
                    "names": [
                        "utest.runner.Framework"
                    ]
                },
                {
                    "names": [
                        "munit.Framework"
                    ]
                }
            ],
            "options": {
                "excludes": [
                    
                ],
                "arguments": [
                    {
                        "args": [
                            "-v",
                            "-a"
                        ],
                        "framework": {
                            "names": [
                                "com.novocode.junit.JUnitFramework"
                            ]
                        }
                    }
                ]
            }
        },
        "platform": {
            "name": "jvm",
            "config": {
                "home": "/Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home",
                "options": [
                    
                ]
            },
            "mainClass": [
                
            ],
            "classpath": [
                "/Users/lloyd/Documents/skala/bloop-on-gradle-with-subprojects/.bloop/b/build/classes",
                "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scalatestplus/junit-4-12_2.12/3.1.2.0/2743ef3c3b767525f9b01a1ccf4417858ed63cd6/junit-4-12_2.12-3.1.2.0.jar",
                "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scalatest/scalatest_2.12/3.1.2/3eed6b0c7e1eb8840df24d3e1cd6b93baf2458c7/scalatest_2.12-3.1.2.jar",
                "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scalactic/scalactic_2.12/3.1.2/7b1721e9bd243b280510920526688ceba75d5138/scalactic_2.12-3.1.2.jar",
                "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-lang/scala-reflect/2.12.11/7695010d1f4309a9c4b65be33528e382869ab3c4/scala-reflect-2.12.11.jar",
                "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-lang.modules/scala-xml_2.12/1.2.0/5d38ac30beb8420dd395c0af447ba412158965e6/scala-xml_2.12-1.2.0.jar",
                "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-lang/scala-library/2.12.11/1a0634714a956c1aae9abefc83acaf6d4eabfa7d/scala-library-2.12.11.jar",
                "/Users/lloyd/Documents/skala/bloop-on-gradle-with-subprojects/.bloop/a/build/classes",
                "/Users/lloyd/.gradle/caches/modules-2/files-2.1/junit/junit/4.12/2973d150c0dc1fefe998f834810d68f278ea58ec/junit-4.12.jar",
                "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.hamcrest/hamcrest-core/1.3/42a25dc3219429f0e5d060061f71acb49bf010a0/hamcrest-core-1.3.jar"
            ]
        },
        "resolution": {
            "modules": [
                {
                    "organization": "org.scalatestplus",
                    "name": "junit-4-12_2.12",
                    "version": "3.1.2.0",
                    "artifacts": [
                        {
                            "name": "junit-4-12_2.12",
                            "path": "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scalatestplus/junit-4-12_2.12/3.1.2.0/2743ef3c3b767525f9b01a1ccf4417858ed63cd6/junit-4-12_2.12-3.1.2.0.jar"
                        },
                        {
                            "name": "junit-4-12_2.12",
                            "classifier": "sources",
                            "path": "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scalatestplus/junit-4-12_2.12/3.1.2.0/2389eca907b198962aadd19f4fe33adebc80fbc4/junit-4-12_2.12-3.1.2.0-sources.jar"
                        }
                    ]
                },
                {
                    "organization": "org.scalatest",
                    "name": "scalatest_2.12",
                    "version": "3.1.2",
                    "artifacts": [
                        {
                            "name": "scalatest_2.12",
                            "path": "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scalatest/scalatest_2.12/3.1.2/3eed6b0c7e1eb8840df24d3e1cd6b93baf2458c7/scalatest_2.12-3.1.2.jar"
                        },
                        {
                            "name": "scalatest_2.12",
                            "classifier": "sources",
                            "path": "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scalatest/scalatest_2.12/3.1.2/4b98d3e92c6fdca49dd2309f43297bc197fbbc2/scalatest_2.12-3.1.2-sources.jar"
                        }
                    ]
                },
                {
                    "organization": "org.scalactic",
                    "name": "scalactic_2.12",
                    "version": "3.1.2",
                    "artifacts": [
                        {
                            "name": "scalactic_2.12",
                            "path": "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scalactic/scalactic_2.12/3.1.2/7b1721e9bd243b280510920526688ceba75d5138/scalactic_2.12-3.1.2.jar"
                        },
                        {
                            "name": "scalactic_2.12",
                            "classifier": "sources",
                            "path": "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scalactic/scalactic_2.12/3.1.2/2efacf642ae772421dde77df616641fa4c111128/scalactic_2.12-3.1.2-sources.jar"
                        }
                    ]
                },
                {
                    "organization": "org.scala-lang",
                    "name": "scala-reflect",
                    "version": "2.12.11",
                    "artifacts": [
                        {
                            "name": "scala-reflect",
                            "path": "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-lang/scala-reflect/2.12.11/7695010d1f4309a9c4b65be33528e382869ab3c4/scala-reflect-2.12.11.jar"
                        },
                        {
                            "name": "scala-reflect",
                            "classifier": "sources",
                            "path": "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-lang/scala-reflect/2.12.11/3acfb99f2620636d281ca7b3d676928ba390de4a/scala-reflect-2.12.11-sources.jar"
                        }
                    ]
                },
                {
                    "organization": "org.scala-lang.modules",
                    "name": "scala-xml_2.12",
                    "version": "1.2.0",
                    "artifacts": [
                        {
                            "name": "scala-xml_2.12",
                            "path": "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-lang.modules/scala-xml_2.12/1.2.0/5d38ac30beb8420dd395c0af447ba412158965e6/scala-xml_2.12-1.2.0.jar"
                        },
                        {
                            "name": "scala-xml_2.12",
                            "classifier": "sources",
                            "path": "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-lang.modules/scala-xml_2.12/1.2.0/97b3021c2ceef1170ff2f0e8a12a1cb5dfc9bfd3/scala-xml_2.12-1.2.0-sources.jar"
                        }
                    ]
                },
                {
                    "organization": "org.scala-lang",
                    "name": "scala-library",
                    "version": "2.12.11",
                    "artifacts": [
                        {
                            "name": "scala-library",
                            "path": "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-lang/scala-library/2.12.11/1a0634714a956c1aae9abefc83acaf6d4eabfa7d/scala-library-2.12.11.jar"
                        },
                        {
                            "name": "scala-library",
                            "classifier": "sources",
                            "path": "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-lang/scala-library/2.12.11/10a1c93ef50b6f1ca7c2223c8d84fab484222731/scala-library-2.12.11-sources.jar"
                        }
                    ]
                },
                {
                    "organization": "junit",
                    "name": "junit",
                    "version": "4.12",
                    "artifacts": [
                        {
                            "name": "junit",
                            "path": "/Users/lloyd/.gradle/caches/modules-2/files-2.1/junit/junit/4.12/2973d150c0dc1fefe998f834810d68f278ea58ec/junit-4.12.jar"
                        },
                        {
                            "name": "junit",
                            "classifier": "sources",
                            "path": "/Users/lloyd/.gradle/caches/modules-2/files-2.1/junit/junit/4.12/a6c32b40bf3d76eca54e3c601e5d1470c86fcdfa/junit-4.12-sources.jar"
                        }
                    ]
                },
                {
                    "organization": "org.hamcrest",
                    "name": "hamcrest-core",
                    "version": "1.3",
                    "artifacts": [
                        {
                            "name": "hamcrest-core",
                            "path": "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.hamcrest/hamcrest-core/1.3/42a25dc3219429f0e5d060061f71acb49bf010a0/hamcrest-core-1.3.jar"
                        },
                        {
                            "name": "hamcrest-core",
                            "classifier": "sources",
                            "path": "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.hamcrest/hamcrest-core/1.3/1dc37250fbc78e23a65a67fbbaf71d2e9cbc3c0b/hamcrest-core-1.3-sources.jar"
                        }
                    ]
                }
            ]
        },
        "tags": [
            "test"
        ]
    }
}
```
</details>

### If we add the dependencies manually

Added to `dependencies`, `classPath`, and `platform.classPath`

<details>

```json
{
    "version": "1.4.0",
    "project": {
        "name": "b-test",
        "directory": "/Users/lloyd/Documents/skala/bloop-on-gradle-with-subprojects/b",
        "workspaceDir": "/Users/lloyd/Documents/skala/bloop-on-gradle-with-subprojects",
        "sources": [
            "/Users/lloyd/Documents/skala/bloop-on-gradle-with-subprojects/b/src/test/scala",
            "/Users/lloyd/Documents/skala/bloop-on-gradle-with-subprojects/b/src/test/java"
        ],
        "dependencies": [
            "b",
            "a",
            "a-test"
        ],
        "classpath": [
            "/Users/lloyd/Documents/skala/bloop-on-gradle-with-subprojects/.bloop/a-test/build/classes",
            "/Users/lloyd/Documents/skala/bloop-on-gradle-with-subprojects/.bloop/b/build/classes",
            "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scalatestplus/junit-4-12_2.12/3.1.2.0/2743ef3c3b767525f9b01a1ccf4417858ed63cd6/junit-4-12_2.12-3.1.2.0.jar",
            "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scalatest/scalatest_2.12/3.1.2/3eed6b0c7e1eb8840df24d3e1cd6b93baf2458c7/scalatest_2.12-3.1.2.jar",
            "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scalactic/scalactic_2.12/3.1.2/7b1721e9bd243b280510920526688ceba75d5138/scalactic_2.12-3.1.2.jar",
            "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-lang/scala-reflect/2.12.11/7695010d1f4309a9c4b65be33528e382869ab3c4/scala-reflect-2.12.11.jar",
            "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-lang.modules/scala-xml_2.12/1.2.0/5d38ac30beb8420dd395c0af447ba412158965e6/scala-xml_2.12-1.2.0.jar",
            "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-lang/scala-library/2.12.11/1a0634714a956c1aae9abefc83acaf6d4eabfa7d/scala-library-2.12.11.jar",
            "/Users/lloyd/Documents/skala/bloop-on-gradle-with-subprojects/.bloop/a/build/classes",
            "/Users/lloyd/.gradle/caches/modules-2/files-2.1/junit/junit/4.12/2973d150c0dc1fefe998f834810d68f278ea58ec/junit-4.12.jar",
            "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.hamcrest/hamcrest-core/1.3/42a25dc3219429f0e5d060061f71acb49bf010a0/hamcrest-core-1.3.jar"
        ],
        "out": "/Users/lloyd/Documents/skala/bloop-on-gradle-with-subprojects/.bloop/b-test/build",
        "classesDir": "/Users/lloyd/Documents/skala/bloop-on-gradle-with-subprojects/.bloop/b-test/build/classes",
        "resources": [
            "/Users/lloyd/Documents/skala/bloop-on-gradle-with-subprojects/b/src/test/resources"
        ],
        "scala": {
            "organization": "org.scala-lang",
            "name": "scala-compiler",
            "version": "2.12.11",
            "options": [
                "-deprecation",
                "-unchecked"
            ],
            "jars": [
                "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-lang/scala-compiler/2.12.11/b93a7407c66f94ebe76c25a1fef17b0b8ecdaf1/scala-compiler-2.12.11.jar",
                "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-sbt/compiler-bridge_2.12/1.3.5/7970db798162352a0424936b1b1680b7478ecd5d/compiler-bridge_2.12-1.3.5-sources.jar",
                "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-sbt/compiler-interface/1.3.5/1f7379ff56b1795b523838cd06ff24272104c380/compiler-interface-1.3.5.jar",
                "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-lang/scala-reflect/2.12.11/7695010d1f4309a9c4b65be33528e382869ab3c4/scala-reflect-2.12.11.jar",
                "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-lang/scala-library/2.12.11/1a0634714a956c1aae9abefc83acaf6d4eabfa7d/scala-library-2.12.11.jar",
                "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-lang.modules/scala-xml_2.12/1.0.6/e22de3366a698a9f744106fb6dda4335838cf6a7/scala-xml_2.12-1.0.6.jar",
                "/Users/lloyd/.gradle/caches/modules-2/files-2.1/com.google.protobuf/protobuf-java/3.7.0/dbb5e9230a91f2a6d011096c2b9c10a5a6e5f7f2/protobuf-java-3.7.0.jar",
                "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-sbt/util-interface/1.3.0/edc7556e112da142bf7e9ead1440d024fd3314c4/util-interface-1.3.0.jar"
            ],
            "setup": {
                "order": "java->scala",
                "addLibraryToBootClasspath": true,
                "addCompilerToClasspath": false,
                "addExtraJarsToClasspath": false,
                "manageBootClasspath": true,
                "filterLibraryFromClasspath": true
            }
        },
        "java": {
            "options": [
                "-source",
                "1.8",
                "-target",
                "1.8",
                "-h",
                "/Users/lloyd/Documents/skala/bloop-on-gradle-with-subprojects/b/build/generated/sources/headers/java/test",
                "-g",
                "-sourcepath",
                "-proc:none",
                "-XDuseUnsharedTable=true"
            ]
        },
        "test": {
            "frameworks": [
                {
                    "names": [
                        "com.novocode.junit.JUnitFramework"
                    ]
                },
                {
                    "names": [
                        "org.scalatest.tools.Framework",
                        "org.scalatest.tools.ScalaTestFramework"
                    ]
                },
                {
                    "names": [
                        "org.scalacheck.ScalaCheckFramework"
                    ]
                },
                {
                    "names": [
                        "org.specs.runner.SpecsFramework",
                        "org.specs2.runner.Specs2Framework",
                        "org.specs2.runner.SpecsFramework"
                    ]
                },
                {
                    "names": [
                        "utest.runner.Framework"
                    ]
                },
                {
                    "names": [
                        "munit.Framework"
                    ]
                }
            ],
            "options": {
                "excludes": [
                    
                ],
                "arguments": [
                    {
                        "args": [
                            "-v",
                            "-a"
                        ],
                        "framework": {
                            "names": [
                                "com.novocode.junit.JUnitFramework"
                            ]
                        }
                    }
                ]
            }
        },
        "platform": {
            "name": "jvm",
            "config": {
                "home": "/Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home",
                "options": [
                    
                ]
            },
            "mainClass": [
                
            ],
            "classpath": [
                "/Users/lloyd/Documents/skala/bloop-on-gradle-with-subprojects/.bloop/a-test/build/classes",
                "/Users/lloyd/Documents/skala/bloop-on-gradle-with-subprojects/.bloop/b/build/classes",
                "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scalatestplus/junit-4-12_2.12/3.1.2.0/2743ef3c3b767525f9b01a1ccf4417858ed63cd6/junit-4-12_2.12-3.1.2.0.jar",
                "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scalatest/scalatest_2.12/3.1.2/3eed6b0c7e1eb8840df24d3e1cd6b93baf2458c7/scalatest_2.12-3.1.2.jar",
                "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scalactic/scalactic_2.12/3.1.2/7b1721e9bd243b280510920526688ceba75d5138/scalactic_2.12-3.1.2.jar",
                "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-lang/scala-reflect/2.12.11/7695010d1f4309a9c4b65be33528e382869ab3c4/scala-reflect-2.12.11.jar",
                "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-lang.modules/scala-xml_2.12/1.2.0/5d38ac30beb8420dd395c0af447ba412158965e6/scala-xml_2.12-1.2.0.jar",
                "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-lang/scala-library/2.12.11/1a0634714a956c1aae9abefc83acaf6d4eabfa7d/scala-library-2.12.11.jar",
                "/Users/lloyd/Documents/skala/bloop-on-gradle-with-subprojects/.bloop/a/build/classes",
                "/Users/lloyd/.gradle/caches/modules-2/files-2.1/junit/junit/4.12/2973d150c0dc1fefe998f834810d68f278ea58ec/junit-4.12.jar",
                "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.hamcrest/hamcrest-core/1.3/42a25dc3219429f0e5d060061f71acb49bf010a0/hamcrest-core-1.3.jar"
            ]
        },
        "resolution": {
            "modules": [
                {
                    "organization": "org.scalatestplus",
                    "name": "junit-4-12_2.12",
                    "version": "3.1.2.0",
                    "artifacts": [
                        {
                            "name": "junit-4-12_2.12",
                            "path": "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scalatestplus/junit-4-12_2.12/3.1.2.0/2743ef3c3b767525f9b01a1ccf4417858ed63cd6/junit-4-12_2.12-3.1.2.0.jar"
                        },
                        {
                            "name": "junit-4-12_2.12",
                            "classifier": "sources",
                            "path": "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scalatestplus/junit-4-12_2.12/3.1.2.0/2389eca907b198962aadd19f4fe33adebc80fbc4/junit-4-12_2.12-3.1.2.0-sources.jar"
                        }
                    ]
                },
                {
                    "organization": "org.scalatest",
                    "name": "scalatest_2.12",
                    "version": "3.1.2",
                    "artifacts": [
                        {
                            "name": "scalatest_2.12",
                            "path": "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scalatest/scalatest_2.12/3.1.2/3eed6b0c7e1eb8840df24d3e1cd6b93baf2458c7/scalatest_2.12-3.1.2.jar"
                        },
                        {
                            "name": "scalatest_2.12",
                            "classifier": "sources",
                            "path": "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scalatest/scalatest_2.12/3.1.2/4b98d3e92c6fdca49dd2309f43297bc197fbbc2/scalatest_2.12-3.1.2-sources.jar"
                        }
                    ]
                },
                {
                    "organization": "org.scalactic",
                    "name": "scalactic_2.12",
                    "version": "3.1.2",
                    "artifacts": [
                        {
                            "name": "scalactic_2.12",
                            "path": "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scalactic/scalactic_2.12/3.1.2/7b1721e9bd243b280510920526688ceba75d5138/scalactic_2.12-3.1.2.jar"
                        },
                        {
                            "name": "scalactic_2.12",
                            "classifier": "sources",
                            "path": "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scalactic/scalactic_2.12/3.1.2/2efacf642ae772421dde77df616641fa4c111128/scalactic_2.12-3.1.2-sources.jar"
                        }
                    ]
                },
                {
                    "organization": "org.scala-lang",
                    "name": "scala-reflect",
                    "version": "2.12.11",
                    "artifacts": [
                        {
                            "name": "scala-reflect",
                            "path": "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-lang/scala-reflect/2.12.11/7695010d1f4309a9c4b65be33528e382869ab3c4/scala-reflect-2.12.11.jar"
                        },
                        {
                            "name": "scala-reflect",
                            "classifier": "sources",
                            "path": "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-lang/scala-reflect/2.12.11/3acfb99f2620636d281ca7b3d676928ba390de4a/scala-reflect-2.12.11-sources.jar"
                        }
                    ]
                },
                {
                    "organization": "org.scala-lang.modules",
                    "name": "scala-xml_2.12",
                    "version": "1.2.0",
                    "artifacts": [
                        {
                            "name": "scala-xml_2.12",
                            "path": "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-lang.modules/scala-xml_2.12/1.2.0/5d38ac30beb8420dd395c0af447ba412158965e6/scala-xml_2.12-1.2.0.jar"
                        },
                        {
                            "name": "scala-xml_2.12",
                            "classifier": "sources",
                            "path": "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-lang.modules/scala-xml_2.12/1.2.0/97b3021c2ceef1170ff2f0e8a12a1cb5dfc9bfd3/scala-xml_2.12-1.2.0-sources.jar"
                        }
                    ]
                },
                {
                    "organization": "org.scala-lang",
                    "name": "scala-library",
                    "version": "2.12.11",
                    "artifacts": [
                        {
                            "name": "scala-library",
                            "path": "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-lang/scala-library/2.12.11/1a0634714a956c1aae9abefc83acaf6d4eabfa7d/scala-library-2.12.11.jar"
                        },
                        {
                            "name": "scala-library",
                            "classifier": "sources",
                            "path": "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.scala-lang/scala-library/2.12.11/10a1c93ef50b6f1ca7c2223c8d84fab484222731/scala-library-2.12.11-sources.jar"
                        }
                    ]
                },
                {
                    "organization": "junit",
                    "name": "junit",
                    "version": "4.12",
                    "artifacts": [
                        {
                            "name": "junit",
                            "path": "/Users/lloyd/.gradle/caches/modules-2/files-2.1/junit/junit/4.12/2973d150c0dc1fefe998f834810d68f278ea58ec/junit-4.12.jar"
                        },
                        {
                            "name": "junit",
                            "classifier": "sources",
                            "path": "/Users/lloyd/.gradle/caches/modules-2/files-2.1/junit/junit/4.12/a6c32b40bf3d76eca54e3c601e5d1470c86fcdfa/junit-4.12-sources.jar"
                        }
                    ]
                },
                {
                    "organization": "org.hamcrest",
                    "name": "hamcrest-core",
                    "version": "1.3",
                    "artifacts": [
                        {
                            "name": "hamcrest-core",
                            "path": "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.hamcrest/hamcrest-core/1.3/42a25dc3219429f0e5d060061f71acb49bf010a0/hamcrest-core-1.3.jar"
                        },
                        {
                            "name": "hamcrest-core",
                            "classifier": "sources",
                            "path": "/Users/lloyd/.gradle/caches/modules-2/files-2.1/org.hamcrest/hamcrest-core/1.3/1dc37250fbc78e23a65a67fbbaf71d2e9cbc3c0b/hamcrest-core-1.3-sources.jar"
                        }
                    ]
                }
            ]
        },
        "tags": [
            "test"
        ]
    }
}
```

</details>

### blooo test works for `b` as well

```shell
❯ bloop test b
BSpec:
- should yo
- should lol
Execution took 15ms
2 tests, 2 passed
All tests in com.beachape.BSpec passed

===============================================
Total duration: 15ms
All 1 test suites passed.
===============================================
```