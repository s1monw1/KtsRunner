# KtsRunner
[![Build Status](https://travis-ci.org/s1monw1/KtsRunner.svg?branch=master)](https://travis-ci.org/s1monw1/KtsRunner)
___
**KtsRunner** is a light-weight tool that allows the execution of `.kts` (Kotlin Script) files from ordinary Kotlin programs.
It's enabled by [JSR 223](https://www.jcp.org/en/jsr/detail?id=223) (Java Scripting Engines API).

## Usage

### Running a script from file system

A simple usage example for KtsRunner can be described as follows:
The declaration of a class is placed in a `.kts` file, which is supposed to be loaded into a normal Kotlin program so that it
can be processed further.

1. Example class
```kotlin
data class ClassFromScript(val x: String)
```

2. `.kts` file
```kotlin
import de.swirtz.ktsrunner.objectloader.ClassFromScript

ClassFromScript("I was created in kts")
```

3. Code to load the object

```kotlin
val scriptReader = Files.newBufferedReader(Paths.get("path/classDeclaration.kts"))
val loadedObj: ClassFromScript = KtsObjectLoader().load<ClassFromScript>(scriptReader)
println(loadedObj.x)
// >> I was created in kts
```

As shown, the `KtsObjectLoader` can be used for executing a `.kts` script and getting its result. The example shows a script that creates an instance of the `ClassFromScript` type that is loaded via ``KtsObjectLoader`` and then processed in the regular program.

### Executing scripts directly

The `KtsObjectLoader` also allows the evaluation of simple `String` based scripts:

```kotlin
val scriptContent = "5 + 10"
val fromScript: Int = KtsObjectLoader().load<Int>(scriptContent))
println(fromScript)
// >> 15
```


### Application Area

You might want to use **KtsRunner** when some part of your application's source has to be outsourced from the regular code. As an example, you can think of an application that provides a test suite runtime. The actual test cases are provided by a QA team which writes their test scripts using a **domain specific language** that is provided by the main application. Since you don't want QA to add source files (defining new test cases) to your application all the time, the test case creation is made via independent `.kts` (Kotlin Scripting) files in which the DSL is being utilized. The test suite main application can use the presented **KtsRunner** library for loading the test cases provided in `.kts` files and process them further afterward.

### Controlling the ClassLoader

When instantiating an `KtsObjectLoader`, you can provide an explicit classloader as shown in this test case:

```kotlin
 @Test
    fun `when passing a custom classloader, it should be used when loading the script`() {
        val myCl = object : ClassLoader() {
            override fun loadClass(name: String?): Class<*> {
                throw IllegalStateException()
            }
        }
       assertExceptionThrownBy<IllegalStateException> {
           KtsObjectLoader(myCl).load("anything")
       }
    }
```
## Getting Started

In your Gradle build, simply include the following repository and dependency:

```kotlin
maven { 
    setUrl("https://dl.bintray.com/s1m0nw1/KtsRunner")
}
```

```kotlin
dependencies {
    //...
    implementation("de.swirtz:ktsRunner:0.0.x")
}
```

