package de.swirtz.ktsrunner.objectloader

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.jetbrains.kotlin.config.KotlinCompilerVersion
import org.jetbrains.kotlin.script.jsr223.KotlinJsr223JvmLocalScriptEngine
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths
import javax.script.ScriptEngine

class KtsObjectLoaderTest {

    @Test
    fun `general ScriptEngineFactory test`() {
        with(KtsObjectLoader().engine.factory) {
            assertThat(languageName).isEqualTo("kotlin")
            assertThat(languageVersion).isEqualTo(KotlinCompilerVersion.VERSION)
            assertThat(engineName).isEqualTo("kotlin")
            assertThat(engineVersion).isEqualTo(KotlinCompilerVersion.VERSION)
            assertThat(extensions).contains("kts")
            assertThat(mimeTypes).contains("text/x-kotlin")
            assertThat(names).contains("kotlin")
            assertThat(
                getMethodCallSyntax(
                    "obj",
                    "method",
                    "arg1",
                    "arg2",
                    "arg3"
                )
            ).isEqualTo("obj.method(arg1, arg2, arg3)")
            assertThat(getOutputStatement("Hello, world!")).isEqualTo("print(\"Hello, world!\")")
            assertThat(getParameter(ScriptEngine.LANGUAGE_VERSION)).isEqualTo(KotlinCompilerVersion.VERSION)
            val sep = System.getProperty("line.separator")
            val prog = arrayOf("val x: Int = 3", "var y = x + 2")
            assertThat(getProgram(*prog)).isEqualTo(prog.joinToString(sep) + sep)
        }
    }

    @Test
    fun `simple evaluations should work`() {
        with(KtsObjectLoader().engine as KotlinJsr223JvmLocalScriptEngine) {
            val res1 = eval("val x = 3")
            assertThat(res1).isEqualTo(null)
            val res2 = eval("x + 2")
            assertThat(res2).isEqualTo(5).describedAs("Reusing x = 3 from prior statement.")
            val fromScript = compile("""listOf(1,2,3).joinToString(":")""")
            assertThat(fromScript.eval()).isEqualTo(listOf(1, 2, 3).joinToString(":"))
        }
    }

    @Test
    fun `when loading expression from script it should result in an integer`() {
        val scriptContent = "5 + 10"
        assertThat(KtsObjectLoader().load<Int>(scriptContent)).isEqualTo(15)
    }

    @Test
    fun `when loading class from string script the content should be as expected`() {

        val scriptContent = Files.readAllBytes(Paths.get("src/test/resources/testscript.kts"))?.let {
            String(it)
        } ?: fail("Cannot load script")

        val loaded = KtsObjectLoader().load<ClassFromScript>(scriptContent)
        assertThat(loaded.text).isEqualTo("I was created in kts; äö")
        assertThat(loaded::class).isEqualTo(ClassFromScript::class)
    }

    @Test
    fun `when loading script with unexpected type, it should result in an IllegalArgumentException`() {
        assertExceptionThrownBy<IllegalArgumentException> {
            KtsObjectLoader().load<String>("5+1")
        }
    }

    @Test
    fun `when loading script with flawed script, then a LoadException should be raised`() {
        assertExceptionThrownBy<LoadException> {
            KtsObjectLoader().load<Int>("Hello World")
        }
    }

    val script1 = "src/test/resources/testscript.kts"
    val script2 = "src/test/resources/testscript2.kts"

    @Test
    fun `when loading class from script via Reader the content should be as expected`() {
        val scriptContent = Files.newBufferedReader(Paths.get(script1))
        val loaded = KtsObjectLoader().load<ClassFromScript>(scriptContent)
        assertThat(loaded.text).isEqualTo("I was created in kts; äö")
        assertThat(loaded::class).isEqualTo(ClassFromScript::class)
    }

    @Test
    fun `when loading class from script via InputStream the content should be as expected`() {
        val scriptContent = Files.newInputStream(Paths.get(script1))
        val loaded = KtsObjectLoader().load<ClassFromScript>(scriptContent)
        assertThat(loaded.text).isEqualTo("I was created in kts; äö")
        assertThat(loaded::class).isEqualTo(ClassFromScript::class)
    }

    @Test
    fun `when loading multiple classes from script via InputStream, all should have the expected type`() {
        val scriptContent = Files.newInputStream(Paths.get(script1))
        val scriptContent2 = Files.newInputStream(Paths.get(script2))
        assertThat(
            KtsObjectLoader().loadAll<ClassFromScript>(scriptContent, scriptContent2)
        ).allMatch { it::class == ClassFromScript::class }
    }

    @Test
    fun `when passing a custom classloader, it should be used when loading the script`() {
        val myCl = object : ClassLoader() {
            override fun loadClass(name: String?): Class<*> {
                throw IllegalStateException()
            }
        }
        assertExceptionThrownBy<IllegalStateException> {
            KtsObjectLoader(myCl).load("anything")
        }
    }
}