package com.example.konsist.libraries.rxjava

import com.example.konsist.Assertions.assertFalse
import com.example.konsist.KonsistUtils.classesProduction
import com.example.konsist.LintRuleMessage
import com.lemonappdev.konsist.api.ext.list.withTextContaining
import io.kotest.core.spec.style.BehaviorSpec

class BehaviorSubjectsHaveDefaultValue : BehaviorSpec() {

    init {
        Given("A class in production") {
            val classes = classesProduction

            When("It has a BehaviorSubject") {
                // withTextContaining, not withText: withText(String) is a whole-text equality
                // match in Konsist, which silently matches nothing.
                val classesWithBehaviorSubject = classes.withTextContaining("BehaviorSubject")

                Then("It has a default value") {
                    classesWithBehaviorSubject.assertFalse(message = Message) {
                        it.text.contains("BehaviorSubject.create()")
                    }
                }
            }
        }
    }

    private companion object {
        private val Message = LintRuleMessage(
            rule = "BehaviorSubjects must be created with a default value.",
            why = """
                A BehaviorSubject without an initial value emits nothing until someone calls
                onNext, so late subscribers (like a recomposing screen) see no state at all.
                An explicit initial value guarantees the UI always has something to render.
            """.trimIndent(),
            howToFix = "Use BehaviorSubject.createDefault(initialValue). For absent values use an empty list or Optional.empty().",
            badExample = """
                private val _state = BehaviorSubject.create<UiState>()
            """.trimIndent(),
            goodExample = """
                private val _state = BehaviorSubject.createDefault(UiState.Initial)
            """.trimIndent(),
        )
    }
}
