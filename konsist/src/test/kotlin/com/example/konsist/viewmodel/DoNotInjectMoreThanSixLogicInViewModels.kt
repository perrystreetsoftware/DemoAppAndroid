package com.example.konsist.viewmodel

import com.example.konsist.Assertions.assertEmpty
import com.example.konsist.KonsistUtils.viewModelsProduction
import com.example.konsist.LintRuleMessage
import com.lemonappdev.konsist.api.ext.list.constructors
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import io.kotest.core.spec.style.BehaviorSpec

class DoNotInjectMoreThanSixLogicInViewModels : BehaviorSpec() {

    init {
        Given("Any viewmodel class in production") {
            val viewModelClasses = viewModelsProduction

            When("It defines a constructor") {
                val constructors = viewModelClasses.constructors

                Then("There are no constructors with more than 6 logic classes") {
                    constructors.filter {
                        val params = it.parameters.withNameEndingWith("Logic")

                        return@filter params.count() > 6
                    }.assertEmpty(message = Message)
                }
            }
        }
    }

    private companion object {
        private val Message = LintRuleMessage(
            rule = "Do not inject more than 6 Logic classes into a ViewModel constructor.",
            why = """
                A ViewModel that needs seven or more use cases is orchestrating too many concerns
                at once. It becomes hard to test, hard to reason about, and every screen change
                risks breaking unrelated behavior.
            """.trimIndent(),
            howToFix = "Split the screen into smaller ViewModels, one per cohesive area of the UI.",
            badExample = """
                class ProfileViewModel(
                    l1: ALogic, l2: BLogic, l3: CLogic, l4: DLogic,
                    l5: ELogic, l6: FLogic, l7: GLogic,
                ) : LifecycleViewModel()
            """.trimIndent(),
            goodExample = """
                class ProfileHeaderViewModel(l1: ALogic, l2: BLogic) : LifecycleViewModel()
                class ProfileContentViewModel(l3: CLogic, l4: DLogic) : LifecycleViewModel()
            """.trimIndent(),
        )
    }
}
