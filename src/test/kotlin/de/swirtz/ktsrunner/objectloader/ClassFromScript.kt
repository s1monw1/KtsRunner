package de.swirtz.ktsrunner.objectloader

data class ClassFromScript(val x: String) {
    fun printme() = println("ClassFromScript with x=$x")
}