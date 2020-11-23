package de.swirtz.ktsrunner.objectloader

import java.io.InputStream
import java.io.Reader
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

/**
 * This class is not thread-safe, don't use it for parallel executions and create new instances instead.
 */
public class KtsObjectLoader(classLoader: ClassLoader? = Thread.currentThread().contextClassLoader) {

    public val engine: ScriptEngine = ScriptEngineManager(classLoader).getEngineByExtension("kts")

    public inline fun <reified T> Any?.castOrError(): T = takeIf { it is T }?.let { it as T }
        ?: throw IllegalArgumentException("Cannot cast $this to expected type ${T::class}")

    public inline fun <reified T> load(script: String): T =
        kotlin.runCatching { engine.eval(script) }
            .getOrElse { throw LoadException("Cannot load script", it) }
            .castOrError()

    public inline fun <reified T> load(reader: Reader): T =
        kotlin.runCatching { engine.eval(reader) }
            .getOrElse { throw LoadException("Cannot load script", it) }
            .castOrError()

    public inline fun <reified T> load(inputStream: InputStream): T = load(inputStream.reader())

    public inline fun <reified T> loadAll(vararg inputStream: InputStream): List<T> = inputStream.map(::load)

}