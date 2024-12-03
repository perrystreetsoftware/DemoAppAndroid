package com.example.konsist.viewmodel

import com.example.konsist.KonsistUtils.viewModelsProduction
import com.lemonappdev.konsist.api.ext.list.properties
import com.lemonappdev.konsist.api.ext.list.types
import com.lemonappdev.konsist.api.verify.assertFalse
import io.kotest.core.spec.style.BehaviorSpec

class DoesNotDependOnRepositories : BehaviorSpec() {

    init {
        Given("A ViewModel") {
            val viewModels = viewModelsProduction

            Then("It does not depend on repositories") {
                viewModels.properties().types.assertFalse(additionalMessage = MESSAGE) {
                    it.name.endsWith("Repository")
                }
            }
        }
    }

    private companion object {
        private const val MESSAGE =
            "ViewModels should not depend on Repositories directly. They should use Logic classes instead."
    }
}
