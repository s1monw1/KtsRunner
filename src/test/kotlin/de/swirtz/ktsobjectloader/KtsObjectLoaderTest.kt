package de.swirtz.ktsobjectloader

import org.jetbrains.kotlin.config.KotlinCompilerVersion
import org.jetbrains.kotlin.script.jsr223.KotlinJsr223JvmLocalScriptEngine
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Paths
import javax.script.ScriptEngine
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.fail


class KtsObjectLoaderTest {

    @Test
    fun `general ScriptEngineFactory test`() {
        KtsObjectLoader.engine.factory.apply {
            assertEquals("kotlin", languageName)
            assertEquals(KotlinCompilerVersion.VERSION, languageVersion)
            assertEquals("kotlin", engineName)
            assertEquals(KotlinCompilerVersion.VERSION, engineVersion)
            assertEquals(listOf("kts"), extensions)
            assertEquals(listOf("text/x-kotlin"), mimeTypes)
            assertEquals(listOf("kotlin"), names)
            assertEquals("obj.method(arg1, arg2, arg3)", getMethodCallSyntax("obj", "method", "arg1", "arg2", "arg3"))
            assertEquals("print(\"Hello, world!\")", getOutputStatement("Hello, world!"))
            assertEquals(KotlinCompilerVersion.VERSION, getParameter(ScriptEngine.LANGUAGE_VERSION))
            val sep = System.getProperty("line.separator")
            val prog = arrayOf("val x: Int = 3", "var y = x + 2")
            assertEquals(prog.joinToString(sep) + sep, getProgram(*prog))
        }
    }

    @Test
    fun `simple evaluations should work`() {
        with(KtsObjectLoader.engine  as KotlinJsr223JvmLocalScriptEngine) {
            val res1 = eval("val x = 3")
            assertNull(res1, "No returned value expected")
            val res2 = eval("x + 2")
            assertEquals(5, res2, "Reusing x = 3 from prior statement.")
            val fromScript = compile("""listOf(1,2,3).joinToString(":")""")
            assertEquals(listOf(1, 2, 3).joinToString(":"), fromScript.eval())
        }
    }

    @Test
    fun `expression from script`() {
        val scriptContent = "5 + 10"

        println(scriptContent)
        assertEquals(15, KtsObjectLoader().load(scriptContent))
    }


    @Test
    fun `class loaded from script`() {
        val scriptContent = Files.readAllBytes(Paths.get("src/test/resources/testscript.kts"))?.let {
            String(it)
        } ?: fail("Cannot load script")

        println(scriptContent)
        assertEquals(ClassFromScript("I was created in kts; äö"), KtsObjectLoader().load(scriptContent))
    }

    @Test
    fun `class loaded from script via Reader`() {
        val scriptContent = Files.newBufferedReader(Paths.get("src/test/resources/testscript.kts"))
        assertEquals(ClassFromScript::class, KtsObjectLoader().load<ClassFromScript>(scriptContent)::class)
    }

}