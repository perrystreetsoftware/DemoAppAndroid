package com.example.konsist.viewmodel

import com.example.konsist.Assertions.assertTrue
import com.example.konsist.KonsistUtils.viewModelsProduction
import com.example.konsist.LintRuleMessage
import io.kotest.core.spec.style.BehaviorSpec

class InheritsFromLifecycleViewModel : BehaviorSpec() {

    init {
        Given("A ViewModel") {
            val viewModels = viewModelsProduction

            Then("It inherits from LifecycleViewModel") {
                viewModels.assertTrue(message = Message, baseline = allowed) {
                    it.hasParentWithName("LifecycleViewModel")
                }
            }
        }
    }

    private companion object {
        private val Message = LintRuleMessage(
            rule = "ViewModels must inherit from LifecycleViewModel.",
            why = """
                LifecycleViewModel centralizes disposables management, error state, and view
                lifecycle callbacks (onFirstAppear/onEveryAppear). Inheriting directly from
                androidx ViewModel loses all of that and fragments our conventions.
            """.trimIndent(),
            howToFix = "Change the superclass to LifecycleViewModel.",
            badExample = "class MyViewModel : ViewModel()",
            goodExample = "class MyViewModel : LifecycleViewModel()",
        )

        // LifecycleViewModel is the base class itself, it cannot inherit from itself.
        private val allowed = arrayOf(
            "LifecycleViewModel",
        )
    }
}
