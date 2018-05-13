package de.swirtz.ktsobjectloader

import java.io.Reader
import javax.script.ScriptEngineManager

class KtsObjectLoader {

    companion object {
        val engine = ScriptEngineManager().getEngineByExtension("kts")
    }

    inline fun <reified T> load(script: String): T {
        val loaded: Any = engine.eval(script)
        return if (loaded is T) {
            loaded
        } else {
            throw IllegalStateException("Could not load script from .kts")
        }
    }

    inline fun <reified T> load(reader: Reader): T {
        val loaded: Any = engine.eval(reader)
        return if (loaded is T) {
            loaded
        } else {
            throw IllegalStateException("Could not load script from .kts")
        }
    }

}

