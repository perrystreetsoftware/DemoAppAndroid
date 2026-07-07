package com.example.konsist.ui

import com.example.konsist.Assertions.assertFalse
import com.example.konsist.KonsistUtils
import com.example.konsist.LintRuleMessage
import com.lemonappdev.konsist.api.ext.list.withTextContaining
import com.lemonappdev.konsist.api.ext.list.withoutAnnotationNamed
import io.kotest.core.spec.style.BehaviorSpec

class ComposableComponentsShouldNotHaveMutableStateOf : BehaviorSpec() {

    init {
        Given("A Composable function") {
            val composableFunctions = KonsistUtils.composableFunctions.withoutAnnotationNamed("Preview")

            When("The composable remembers state") {
                val remembering = composableFunctions.withTextContaining("remember {")

                Then("It should not mutate the state") {
                    remembering.assertFalse(message = Message) {
                        Regex("mutable\\w*Of").find(it.text) != null
                    }
                }
            }
        }
    }

    private companion object {
        private val Message = LintRuleMessage(
            rule = "Composable functions must not call mutableStateOf (or its variants) inside remember { }.",
            why = """
                Retaining mutable state inside a Composable couples UI to state ownership and makes
                the behavior impossible to unit test. Hoisting state to the ViewModel keeps the
                Composable a pure function of its inputs and avoids subtle recomposition bugs.
            """.trimIndent(),
            howToFix = """
                Hoist the state to the ViewModel: expose it via an Observable state from a
                BehaviorSubject and update it through ViewModel functions invoked by the
                Composable's callbacks. The Composable should only consume the state, not own it.
            """.trimIndent(),
            badExample = """
                @Composable
                fun MyScreen() {
                    val count = remember { mutableStateOf(0) }
                    Button(onClick = { count.value++ }) { Text(count.value.toString()) }
                }
            """.trimIndent(),
            goodExample = """
                @Composable
                fun MyScreen(state: UiState, onIncrement: () -> Unit) {
                    Button(onClick = onIncrement) { Text(state.count.toString()) }
                }
            """.trimIndent(),
        )
    }
}
