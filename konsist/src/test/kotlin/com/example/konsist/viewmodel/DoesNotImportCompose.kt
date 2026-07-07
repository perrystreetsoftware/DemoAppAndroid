package com.example.konsist.viewmodel

import com.example.konsist.Assertions.assertEmpty
import com.example.konsist.KonsistUtils
import com.example.konsist.LintRuleMessage
import com.lemonappdev.konsist.api.ext.list.withImport
import io.kotest.core.spec.style.BehaviorSpec

class DoesNotImportCompose : BehaviorSpec() {

    init {
        Given("The ViewModels layer") {
            val viewModelFiles = KonsistUtils.viewModelsModule.files
                .withImport {
                    // Match any androidx.compose.* package EXCEPT androidx.compose.runtime.*
                    it.text.contains(Regex("androidx\\.compose\\.(?!runtime\\b)[\\w.]+"))
                }

            Then("It must not import compose source directly") {
                viewModelFiles.assertEmpty(message = Message)
            }
        }
    }

    private companion object {
        private val Message = LintRuleMessage(
            rule = "The ViewModels layer must not import Compose (androidx.compose.runtime is allowed).",
            why = """
                ViewModels are UI-toolkit agnostic: they expose observable state and take action
                calls. Importing Compose UI types couples the presentation logic to the view
                layer and makes ViewModels impossible to test without UI dependencies.
            """.trimIndent(),
            howToFix = "Move the Compose-dependent code into the feature (UI) module and keep only state/actions in the ViewModel.",
            badExample = """
                import androidx.compose.ui.graphics.Color
                class MyViewModel { val titleColor = Color.Red }
            """.trimIndent(),
            goodExample = """
                class MyViewModel { val state: Observable<UiState> = _state }
                // The UI layer maps state to colors.
            """.trimIndent(),
        )
    }
}
