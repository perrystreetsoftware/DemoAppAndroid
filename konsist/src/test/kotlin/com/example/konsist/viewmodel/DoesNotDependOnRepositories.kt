package com.example.konsist.viewmodel

import com.example.konsist.Assertions.assertFalse
import com.example.konsist.KonsistUtils.viewModelsProduction
import com.example.konsist.LintRuleMessage
import com.lemonappdev.konsist.api.ext.list.properties
import com.lemonappdev.konsist.api.ext.list.types
import io.kotest.core.spec.style.BehaviorSpec

class DoesNotDependOnRepositories : BehaviorSpec() {

    init {
        Given("A ViewModel") {
            val viewModels = viewModelsProduction

            Then("It does not depend on repositories") {
                viewModels.properties().types.assertFalse(message = Message) {
                    it.name.endsWith("Repository")
                }
            }
        }
    }

    private companion object {
        private val Message = LintRuleMessage(
            rule = "ViewModels must not depend on Repositories directly.",
            why = """
                The ViewModel layer talks to the domain layer (Logic classes), never to the data
                layer. Skipping the Logic layer spreads business rules across ViewModels and makes
                them impossible to reuse or test in isolation.
            """.trimIndent(),
            howToFix = "Create or reuse a Logic class that wraps the repository call and inject that Logic into the ViewModel.",
            badExample = """
                class MyViewModel(private val repository: CountryListRepository) : LifecycleViewModel()
            """.trimIndent(),
            goodExample = """
                class MyViewModel(private val logic: CountryListLogic) : LifecycleViewModel()
            """.trimIndent(),
        )
    }
}
