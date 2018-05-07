# KtsRunner
[![Build Status](https://travis-ci.org/s1monw1/KtsRunner.svg?branch=master)](https://travis-ci.org/s1monw1/KtsRunner)
___
**KtsRunner** is a light-weight tool that allows the execution of `.kts (Kotlin Script) files from ordinary Kotlin programs.
It's enabled by [JSR 223](https://www.jcp.org/en/jsr/detail?id=223) (Java Scripting Engines API).

## Usage

### Running a script from file system

A simple usage example for KtsRunner can be described as follows:
The definition of a class is placed in a `.kts file, which is loaded into a normal Kotlin program.

1. The example class
```kotlin
data class ClassFromScript(val x: String)
```

2. The `.kts` file
```kotlin
import de.swirtz.ktsobjectloader.ClassFromScript

ClassFromScript("I was created in kts")
```

3. The code to load the class

```kotlin
val scriptReader = Files.newBufferedReader(Paths.get("path/classDeclaration.kts"))
val loadedObj = KtsObjectLoader().load<ClassFromScript>(scriptReader)
```

As shown, the `KtsObjectLoader` class can be used for loading an object from a `.kts` file. Currently, the library only supports single entities coming from the script.

## Getting Started

In your Gradle build, simply include the following repository and dependency:

```kotlin
maven { 
    setUrl("https://dl.bintray.com/s1m0nw1/KtsRunner")
}

compile("de.swirtz:ktsrunner:0.0.x")

```

