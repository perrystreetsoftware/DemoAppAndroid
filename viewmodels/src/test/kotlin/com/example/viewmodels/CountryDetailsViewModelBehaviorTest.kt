package com.example.viewmodels

import com.example.viewmodels.spec.BaseBehaviorSpec
import org.amshove.kluent.shouldBeInstanceOf
import org.koin.core.parameter.parametersOf
import org.koin.test.inject

class CountryDetailsViewModelBehaviorTest : BaseBehaviorSpec() {

    private val viewModel: CountryDetailsViewModel by inject {
        parametersOf("ug")
    }

    init {
        Given("I tap on a country") {
            val testObserver = viewModel.state.test()

            Then("The page is loaded") {
                testObserver.values()
                    .last() shouldBeInstanceOf CountryDetailsViewModel.UiState.Loaded::class
            }
        }
    }
}
