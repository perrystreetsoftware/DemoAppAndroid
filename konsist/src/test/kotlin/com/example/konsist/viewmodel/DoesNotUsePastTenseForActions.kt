package com.example.konsist.viewmodel

import com.example.konsist.KonsistUtils.viewModelsProduction
import com.lemonappdev.konsist.api.ext.list.functions
import com.lemonappdev.konsist.api.ext.list.withNameStartingWith
import com.lemonappdev.konsist.api.ext.list.withoutName
import com.lemonappdev.konsist.api.verify.assertFalse
import io.kotest.core.spec.style.BehaviorSpec

class DoesNotUsePastTenseForActions : BehaviorSpec() {

    init {
        Given("A ViewModel") {
            val viewModels = viewModelsProduction

            When("There is a function for a user action") {
                val functions = viewModels.functions().withNameStartingWith("on").withoutName(*allowed)

                Then("It does not use past tense in its name") {
                    functions.assertFalse(additionalMessage = PAST_TENSE_MESSAGE) {
                        it.name.endsWith("ed")
                    }
                }

                Then("It uses 'tap' instead of 'click' in its name") {
                    functions.assertFalse(additionalMessage = CLICK_MESSAGE) {
                        it.name.contains("click", ignoreCase = true)
                    }
                }
            }
        }
    }

    private companion object {
        private val allowed = arrayOf(
            "onCleared",
        )

        private const val PAST_TENSE_MESSAGE =
            "Verbs in ViewModel functions that represent user actions should not be in past tense. Use present tense instead."

        private const val CLICK_MESSAGE =
            "ViewModel functions that represent user actions should use 'tap' instead of 'click'."
    }
}
