package de.swirtz.sample

import de.swirtz.ktsrunner.objectloader.KtsObjectLoader

fun readRandomValueFromScript(): Int {
    println("Hello Kotlin Scripting World")
    return KtsObjectLoader().load("val x = 10; x")
}