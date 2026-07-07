package com.example.konsist.viewmodel

import com.example.konsist.Assertions.assertFalse
import com.example.konsist.KonsistUtils.viewModelsProduction
import com.example.konsist.LintRuleMessage
import com.lemonappdev.konsist.api.ext.list.properties
import com.lemonappdev.konsist.api.ext.list.withType
import io.kotest.core.spec.style.BehaviorSpec

class DoesNotExposeObservablesViaGetter : BehaviorSpec() {

    init {
        Given("A ViewModel") {
            val viewModels = viewModelsProduction

            When("It has a property of type Observable") {
                val observables = viewModels.properties().withType {
                    it.hasNameContaining("Observable")
                }

                Then("The Observable does not have a custom getter") {
                    observables.assertFalse(message = Message) {
                        it.hasGetter
                    }
                }
            }
        }
    }

    private companion object {
        private val Message = LintRuleMessage(
            rule = "ViewModels must not expose Observables via a custom getter.",
            why = """
                A custom getter builds a brand new Observable on every access. In Compose that means
                every recomposition subscribes to a fresh stream, which can trigger an infinite
                recomposition loop and duplicated side effects.
            """.trimIndent(),
            howToFix = "Assign the Observable once to a val instead of computing it in a getter.",
            badExample = """
                val state: Observable<UiState>
                    get() = _state.map { it }
            """.trimIndent(),
            goodExample = """
                val state: Observable<UiState> = _state
            """.trimIndent(),
        )
    }
}
