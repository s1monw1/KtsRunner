package de.swirtz.ktsobjectloader

import java.io.InputStream
import java.io.Reader
import javax.script.ScriptEngineManager

class KtsObjectLoader {

    companion object {
        val engine = ScriptEngineManager().getEngineByExtension("kts")
    }

    inline fun <reified T> load(script: String): T = engine.eval(script).takeIf { it is T } as T
    ?: throw IllegalStateException("Could not load script from .kts")

    inline fun <reified T> load(reader: Reader): T = engine.eval(reader).takeIf { it is T } as T
        ?: throw IllegalStateException("Could not load script from .kts")

    inline fun <reified T> load(inputStream: InputStream): T = engine.eval(inputStream.reader()).takeIf { it is T } as T
        ?: throw IllegalStateException("Could not load script from .kts")

    inline fun <reified T> loadAll(vararg inputStream: InputStream): List<T> = inputStream.map(::load)
}

