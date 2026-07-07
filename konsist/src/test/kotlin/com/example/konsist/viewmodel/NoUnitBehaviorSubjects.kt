package com.example.konsist.viewmodel

import com.example.konsist.Assertions.assertFalse
import com.example.konsist.KonsistUtils.viewModelsProduction
import com.example.konsist.LintRuleMessage
import com.lemonappdev.konsist.api.ext.list.properties
import io.kotest.core.spec.style.BehaviorSpec

class NoUnitBehaviorSubjects : BehaviorSpec() {

    init {
        Given("A ViewModel") {
            val viewModels = viewModelsProduction

            Then("It does not have a BehaviorSubject that emits Units") {
                viewModels.properties().assertFalse(message = Message) {
                    // We use text.contains("BehaviorSubject") to also capture `behaviorSubjectOf`
                    it.text.contains("BehaviorSubject", ignoreCase = true) &&
                        it.text.contains("Unit")
                }
            }
        }
    }

    private companion object {
        private val Message = LintRuleMessage(
            rule = "ViewModels must not declare BehaviorSubjects that emit Unit.",
            why = """
                A BehaviorSubject<Unit> is a signal with no state — if you need one, you are likely
                working around a piece of state that should be reactive but currently is not.
                Modeling the actual state keeps the UI reproducible from a single source of truth.
            """.trimIndent(),
            howToFix = "Model the underlying state explicitly and emit that state instead of a Unit ping.",
            badExample = """
                private val refreshTrigger = BehaviorSubject.createDefault(Unit)
            """.trimIndent(),
            goodExample = """
                private val _state = BehaviorSubject.createDefault<State>(State.Initial)
            """.trimIndent(),
        )
    }
}
