package com.example.konsist.styling

import com.example.konsist.Assertions.assertEmpty
import com.example.konsist.KonsistUtils
import com.example.konsist.LintRuleMessage
import io.kotest.core.spec.style.BehaviorSpec

class FirstClassInFileMatchesFileName : BehaviorSpec() {

    init {
        Given("I have class inside a file matching its name") {
            val matching = KonsistUtils.classesProduction.filter { it.name == it.containingFile.name }

            Then("The first class on the file should be the class that matches the file name") {
                val violations = matching.filter { it.containingFile.classes().first().name != it.containingFile.name }

                violations.assertEmpty(message = Message)
            }
        }
    }

    private companion object {
        private val Message = LintRuleMessage(
            rule = "A file containing a class with a matching name must start with that class.",
            why = """
                Readers open a file expecting the type it is named after. Burying the main class
                under helper types makes navigation slower and code review harder.
            """.trimIndent(),
            howToFix = """
                Move the main class to the top of the file. Put state, events, or other related
                classes after it, nest them inside it, or extract them to their own file.
            """.trimIndent(),
            badExample = """
                // MyViewModel.kt
                data class Helper(...)
                class MyViewModel : LifecycleViewModel()
            """.trimIndent(),
            goodExample = """
                // MyViewModel.kt
                class MyViewModel : LifecycleViewModel() {
                    data class Helper(...)
                }
            """.trimIndent(),
        )
    }
}
