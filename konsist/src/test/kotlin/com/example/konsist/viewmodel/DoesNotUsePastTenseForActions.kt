package com.example.konsist.viewmodel

import com.example.konsist.Assertions.assertFalse
import com.example.konsist.KonsistUtils.viewModelsProduction
import com.example.konsist.LintRuleMessage
import com.lemonappdev.konsist.api.ext.list.functions
import com.lemonappdev.konsist.api.ext.list.withNameStartingWith
import io.kotest.core.spec.style.BehaviorSpec

class DoesNotUsePastTenseForActions : BehaviorSpec() {

    init {
        Given("A ViewModel") {
            val viewModels = viewModelsProduction

            When("There is a function for a user action") {
                val functions = viewModels.functions().withNameStartingWith("on")

                Then("It does not use past tense in its name") {
                    functions.assertFalse(message = PastTenseMessage, baseline = allowed) {
                        it.name.endsWith("ed")
                    }
                }

                Then("It uses 'tap' instead of 'click' in its name") {
                    functions.assertFalse(message = ClickMessage) {
                        it.name.contains("click", ignoreCase = true)
                    }
                }
            }
        }
    }

    private companion object {
        private val PastTenseMessage = LintRuleMessage(
            rule = "ViewModel functions that represent user actions must not use past tense.",
            why = """
                A user action is a command telling the ViewModel something is happening now, not a
                notification about the past. Present tense keeps intent-based naming consistent
                across all ViewModels.
            """.trimIndent(),
            howToFix = "Rename the function to present tense.",
            badExample = "fun onCountrySelected(country: Country)",
            goodExample = "fun onCountrySelect(country: Country)",
        )

        private val ClickMessage = LintRuleMessage(
            rule = "ViewModel functions that represent user actions must use 'tap' instead of 'click'.",
            why = """
                Our platform vocabulary is touch-first; 'tap' is the interaction users actually
                perform. A single term keeps naming searchable and consistent across the codebase.
            """.trimIndent(),
            howToFix = "Rename the function to use 'tap'.",
            badExample = "fun onRefreshClick()",
            goodExample = "fun onRefreshTap()",
        )

        // onCleared is Android's ViewModel lifecycle callback, its name is not ours to choose.
        private val allowed = arrayOf(
            "onCleared",
        )
    }
}
