package de.swirtz.ktsobjectloader

data class ClassFromScript(val x: String) {
    fun printme() = println("ClassFromScript with x=$x")
}