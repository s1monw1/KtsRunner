package de.swirtz.ktsrunner.objectloader

data class ClassFromScript(val text: String) {
    fun printMe() = println("ClassFromScript with text=$text")
}