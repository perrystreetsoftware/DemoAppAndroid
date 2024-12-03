package com.perrystreet.konsist.viewmodel

import com.example.konsist.KonsistUtils.viewModelsProduction
import com.lemonappdev.konsist.api.ext.list.classes
import com.lemonappdev.konsist.api.ext.list.modifierprovider.withSealedModifier
import com.lemonappdev.konsist.api.ext.list.withNameContaining
import com.lemonappdev.konsist.api.verify.assertFalse
import io.kotest.core.spec.style.BehaviorSpec

class DoesNotExposeErrorEvents : BehaviorSpec() {

    init {
        Given("A ViewModel") {
            val viewModels = viewModelsProduction

            When("It has an Event sealed class") {
                val events = viewModels.classes().withSealedModifier().withNameContaining("Event")

                Then("It does not have an Error class") {
                    events.assertFalse(additionalMessage = MESSAGE) { event ->
                        event.hasClass { it.name.contains("Error") }
                    }
                }

                Then("It does not have an Error object") {
                    events.assertFalse(additionalMessage = MESSAGE) { event ->
                        event.hasObject { it.name.contains("Error") }
                    }
                }
            }
        }
    }

    private companion object {
        private const val MESSAGE =
            "Do not expose errors as events in ViewModels. Use the error observable state available in our LifecycleViewModel instead."
    }
}
