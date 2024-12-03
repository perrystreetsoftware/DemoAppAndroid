package com.example.konsist.viewmodel

import com.example.konsist.KonsistUtils.viewModelsProduction
import com.lemonappdev.konsist.api.ext.list.withoutName
import com.lemonappdev.konsist.api.verify.assertTrue
import io.kotest.core.spec.style.BehaviorSpec

class InheritsFromLifecycleViewModel : BehaviorSpec() {

    init {
        Given("A ViewModel") {
            val viewModels = viewModelsProduction.withoutName(*allowed)

            Then("It inherits from LifecycleViewModel") {
                viewModels.assertTrue(additionalMessage = MESSAGE) {
                    it.hasParentWithName("LifecycleViewModel")
                }
            }
        }
    }

    private companion object {
        private const val MESSAGE =
            "Always inherit from the LifecycleViewModel to get advantage of the lifecycle events it provides"

        private val allowed = arrayOf(
            "LifecycleViewModel",
        )
    }
}
