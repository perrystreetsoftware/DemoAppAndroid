package com.example.konsist.viewmodel

import com.example.konsist.Assertions.assertFalse
import com.example.konsist.KonsistUtils.viewModelsProduction
import com.example.konsist.LintRuleMessage
import io.kotest.core.spec.style.BehaviorSpec

class DoesNotHaveInitializer : BehaviorSpec() {

    init {
        Given("A ViewModel") {
            val viewModels = viewModelsProduction

            Then("It does not have an initializer") {
                viewModels.assertFalse(message = Message, baseline = baseline) {
                    it.hasInitBlocks()
                }
            }
        }
    }

    private companion object {
        private val Message = LintRuleMessage(
            rule = "ViewModels must not have init blocks.",
            why = """
                Work started in init runs as soon as the ViewModel is constructed, before the view
                is visible, which causes side effects that are hard to control and impossible to
                re-trigger. Tying work to lifecycle callbacks keeps it predictable and testable.
            """.trimIndent(),
            howToFix = "Move the init work into an override of LifecycleViewModel#onFirstAppear() or #onEveryAppear().",
            badExample = """
                class MyViewModel(private val logic: MyLogic) : LifecycleViewModel() {
                    init {
                        load()
                    }
                }
            """.trimIndent(),
            goodExample = """
                class MyViewModel(private val logic: MyLogic) : LifecycleViewModel() {
                    override fun onFirstAppear() {
                        load()
                    }
                }
            """.trimIndent(),
        )

        private val baseline = arrayOf(
            "CountryDetailsViewModel",
            "CountryListViewModel",
        )
    }
}
