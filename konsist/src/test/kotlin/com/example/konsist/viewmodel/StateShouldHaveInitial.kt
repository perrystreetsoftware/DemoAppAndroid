package com.example.konsist.viewmodel

import com.example.konsist.Assertions.assertFalse
import com.example.konsist.Assertions.assertTrue
import com.example.konsist.KonsistUtils.viewModelsProduction
import com.example.konsist.LintRuleMessage
import com.lemonappdev.konsist.api.ext.list.classes
import com.lemonappdev.konsist.api.ext.list.modifierprovider.withSealedModifier
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import io.kotest.core.spec.style.BehaviorSpec

class StateShouldHaveInitial : BehaviorSpec() {

    init {
        Given("A ViewModel") {
            val viewModels = viewModelsProduction

            When("It has a sealed state class") {
                val sealedStateClasses = viewModels.classes().withSealedModifier().withNameEndingWith("State")

                Then("It is called State") {
                    sealedStateClasses.assertTrue(message = Message, baseline = baselineNaming) {
                        it.hasNameMatching(allowedStateNames)
                    }
                }

                Then("It resides within the ViewModel") {
                    sealedStateClasses.assertFalse(message = Message) { it.isTopLevel }
                }

                Then("It has a child with an Initial state unless it is called ModalState") {
                    sealedStateClasses.assertTrue(message = Message) { stateClass ->
                        stateClass.hasObject {
                            it.hasNameMatching(initialRegex)
                        } ||
                            stateClass.hasClass {
                                it.hasNameMatching(initialRegex)
                            } ||
                            stateClass.hasNameMatching(Regex("ModalState"))
                    }
                }
            }
        }
    }

    private companion object {
        val allowedStateNames = Regex("(State|ModalState)")
        val initialRegex = Regex("(Initial|None|Empty|Hidden|Idle|Dismissed)")

        private val Message = LintRuleMessage(
            rule = "Sealed state classes in ViewModels must be named State, be nested in the ViewModel, and have an initial case.",
            why = """
                A single name makes state classes discoverable across every ViewModel; nesting ties
                the state to its owner; and an explicit initial case guarantees subscribers always
                have a value to render before any work completes.
            """.trimIndent(),
            howToFix = """
                Rename the sealed class to State, move it inside the ViewModel, and add an
                Initial (or None/Empty/Hidden/Idle/Dismissed) case used as the BehaviorSubject default.
            """.trimIndent(),
            badExample = """
                sealed class ScreenUiState {
                    object Loading : ScreenUiState()
                }
            """.trimIndent(),
            goodExample = """
                class MyViewModel : LifecycleViewModel() {
                    sealed class State {
                        object Initial : State()
                        object Loading : State()
                    }
                }
            """.trimIndent(),
        )

        private val baselineNaming = arrayOf(
            "UiState",
        )
    }
}
