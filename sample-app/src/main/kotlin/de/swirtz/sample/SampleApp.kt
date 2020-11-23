package de.swirtz.sample

import de.swirtz.ktsrunner.objectloader.KtsObjectLoader

fun main() {
    println("Hello Kotlin Scripting World")
    val i = KtsObjectLoader().load<Int>("val x = 10; x")
    println(i)
}