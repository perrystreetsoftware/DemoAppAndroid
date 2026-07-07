package com.example.konsist.ui

import com.example.konsist.Assertions.assertFalse
import com.example.konsist.KonsistUtils.containsAny
import com.example.konsist.KonsistUtils.productionCode
import com.example.konsist.LintRuleMessage
import com.lemonappdev.konsist.api.ext.list.withFunction
import com.lemonappdev.konsist.api.ext.list.withoutNameEndingWith
import io.kotest.core.spec.style.BehaviorSpec

class ViewLayerDoesNotUseIfStatements : BehaviorSpec() {

    init {
        Given("A file with composable functions, excluding the UI components library") {
            // uicomponents is our design-system analog: reusable components may branch
            // internally on their own props, like Husband-Droid's design system does.
            val files = productionCode
                .slice { !it.path.contains("/uicomponents/") }
                .files
                .withoutNameEndingWith("Extensions")
                .withFunction { it.hasAnnotationWithName("Composable") }

            Then("It does not have if statements or ?.let blocks") {
                files.assertFalse(message = Message, baseline = baseline) {
                    it.text.containsAny(listOf("if (", "?.let {", "?.let{"))
                }
            }
        }
    }

    private companion object {
        private val Message = LintRuleMessage(
            rule = "If statements and ?.let blocks are not allowed in the View layer.",
            why = """
                Branching in our View layer means untested logic.
                When we cannot have branching in the View layer, it forces us to think how to make
                that logic testable and how to make it part of the state of the ViewModel.

                The only acceptable case is component visibility, where a guard-style early
                return based on the state is allowed.
            """.trimIndent(),
            howToFix = """
                Either

                1) Use a guard-style early return, or
                2) push the condition into the ViewModel/Logic layer so the state already
                   carries the resolved value the Composable renders.
            """.trimIndent(),
            badExample = """
                @Composable
                fun ServerStatusLabel(status: ServerStatus?) {
                    Text(text = if (status?.ok == true) "Server status is OK" else "Server status is not OK")
                }
            """.trimIndent(),
            goodExample = """
                @Composable
                fun ServerStatusLabel(state: UiState) {
                    Text(text = state.serverStatusLabel)
                }
                // The ViewModel resolves serverStatusLabel from the server status.
            """.trimIndent(),
        )

        private val baseline = arrayOf(
            "CountryListAdapter",
            "CountryListPage",
            "CountryNotFoundErrorView",
            "CountrySelectingButton",
            "CountrySelectingList",
        )
    }
}
