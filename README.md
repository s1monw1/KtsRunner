# Kotlin DSL für die Testworkbench

Projekt stellt eine DSL für die Beschreibung von TWB-Testfällen bereit. Zusätzlich beinhaltet das Projekt einen speziellen ``de.ndesign.testworkbench.kotlindsltestcasedao.KotlinTestCaseLoader`` , der `.kts` (Kotlin Script) Files vom Filesystem laden und ausführen kann. Dieser wird vom `de.ndesign.testworkbench.kotlindsltestcasedao.KotlinScriptDao` genutzt, der die TWB-Schnittstelle `TestCaseDAO` implementiert. 

Die Implementierungen beruhen auf einer OSGi-freien Implementierung der TWB.

## DSL

Der Inhalt einer o.g. `.kts` Datei sieht beispielsweise so aus:

``` kotlin
testCase {
    id = "myFirstTestCase"
    name = "myFirstTestCaseName"
    version = "1"
    description = "test"

    precondition {
        description = "myprecondition"
        modules {
            delay("myid", 10) {
                delayEndTimeKey = "end"
                delayStartTimeKey = "start"

            }
        }
    }
    step {
        description = "first Step"
        expected = "will fail"
        modules {
            delay("myid2", 1010) {
                delayStartTimeKey = "start"
                delayEndTimeKey = "end"
                report("mySecondReport", "start", "txt")
            }
        }
    }
}
```