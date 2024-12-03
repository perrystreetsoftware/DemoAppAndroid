package com.example.konsist.viewmodel

import com.example.konsist.KonsistUtils.viewModelsProduction
import com.lemonappdev.konsist.api.ext.list.withoutName
import com.lemonappdev.konsist.api.verify.assertFalse
import io.kotest.core.spec.style.BehaviorSpec

class DoesNotHaveInitializer : BehaviorSpec() {

    init {
        Given("A ViewModel") {
            val viewModels = viewModelsProduction.withoutName(*baseline)

            Then("It does not have an initializer") {
                viewModels.assertFalse(additionalMessage = MESSAGE) {
                    it.hasInitBlocks()
                }
            }
        }
    }

    private companion object {
        private const val MESSAGE =
            "Avoid using init in ViewModels to prevent side effects. Override #doOnViewAppear() instead"

        private val baseline = arrayOf(
            "CountryDetailsViewModel",
            "CountryListViewModel"
        )
    }
}
