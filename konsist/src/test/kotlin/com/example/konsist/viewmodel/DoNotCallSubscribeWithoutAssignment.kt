package com.example.konsist.viewmodel

import com.example.konsist.Assertions.assertTrue
import com.example.konsist.KonsistUtils.viewModelsProduction
import com.example.konsist.LintRuleMessage
import com.lemonappdev.konsist.api.ext.list.functions
import io.kotest.core.spec.style.BehaviorSpec

class DoNotCallSubscribeWithoutAssignment : BehaviorSpec() {

    init {
        Given("All functions of ViewModels") {
            val viewModelFunctions = viewModelsProduction.functions()

            And("It contains subscribe") {
                val subscribeFunctions = viewModelFunctions
                    .filter {
                        it.text.contains(".subscribe(")
                    }

                Then("It puts that subscription into disposables or stores it somehow") {
                    subscribeFunctions.assertTrue(message = Message) {
                        it.text.contains("disposables +=") ||
                            it.text.contains("disposables.add(") ||
                            it.text.contains("Disposable")
                    }
                }
            }
        }
    }

    private companion object {
        private val Message = LintRuleMessage(
            rule = "The Disposable returned by subscribe() must be captured.",
            why = """
                A subscription that is not added to the ViewModel's disposables keeps running after
                the ViewModel is cleared, leaking memory and emitting into dead state.
            """.trimIndent(),
            howToFix = "Wrap the subscription in disposables.add(...) so it is disposed in onCleared().",
            badExample = """
                fun onRefreshTap() {
                    logic.reload().subscribe()
                }
            """.trimIndent(),
            goodExample = """
                fun onRefreshTap() {
                    disposables.add(logic.reload().subscribe())
                }
            """.trimIndent(),
        )
    }
}
