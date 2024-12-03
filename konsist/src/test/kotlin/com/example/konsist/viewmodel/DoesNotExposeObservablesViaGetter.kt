package com.example.konsist.viewmodel

import com.example.konsist.KonsistUtils.viewModelsProduction
import com.lemonappdev.konsist.api.ext.list.properties
import com.lemonappdev.konsist.api.ext.list.withType
import com.lemonappdev.konsist.api.verify.assertFalse
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
                    observables.assertFalse(additionalMessage = MESSAGE) {
                        it.hasGetter
                    }
                }
            }
        }
    }

    private companion object {
        private const val MESSAGE =
            "Avoid exposing Observables via a custom getter since that can cause an infinite recomposition in Compose."
    }
}
