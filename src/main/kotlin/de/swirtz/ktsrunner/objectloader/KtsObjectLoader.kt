package de.swirtz.ktsrunner.objectloader

import java.io.InputStream
import java.io.Reader
import javax.script.ScriptEngineManager

/**
 * This class is not thread-safe, don't use it for parallel executions and create new instances instead.
 */
class KtsObjectLoader(classLoader: ClassLoader? = Thread.currentThread().contextClassLoader) {

    val engine = ScriptEngineManager(classLoader).getEngineByExtension("kts")

    inline fun <R> safeEval(evaluation: () -> R?) = try {
        evaluation()
    } catch (e: Exception) {
        throw LoadException("Cannot load script", e)
    }

    inline fun <reified T> Any?.castOrError() = takeIf { it is T }?.let { it as T }
        ?: throw IllegalArgumentException("Cannot cast $this to expected type ${T::class}")

    inline fun <reified T> load(script: String): T = safeEval { engine.eval(script) }.castOrError()

    inline fun <reified T> load(reader: Reader): T = safeEval { engine.eval(reader) }.castOrError()

    inline fun <reified T> load(inputStream: InputStream): T = load(inputStream.reader())

    inline fun <reified T> loadAll(vararg inputStream: InputStream): List<T> = inputStream.map(::load)
}

