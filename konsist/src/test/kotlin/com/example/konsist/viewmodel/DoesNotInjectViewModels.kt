package com.example.konsist.viewmodel

import com.example.konsist.Assertions.assertFalse
import com.example.konsist.KonsistUtils
import com.example.konsist.LintRuleMessage
import com.lemonappdev.konsist.api.ext.list.properties
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import io.kotest.core.spec.style.BehaviorSpec

class DoesNotInjectViewModels : BehaviorSpec() {

    init {
        Given("A class in production") {
            val classes = KonsistUtils.classesProduction

            When("It has a ViewModel property") {
                val properties = classes
                    .properties()
                    .withNameEndingWith(suffix = "viewModel", suffixes = arrayOf("ViewModel"))

                Then("None is injected with `by inject()`") {
                    properties.assertFalse(message = Message) {
                        it.text.contains("by inject()")
                    }
                }
            }
        }
    }

    private companion object {
        private val Message = LintRuleMessage(
            rule = "Do not inject ViewModels with `by inject()`.",
            why = """
                Koin's inject() treats the ViewModel as a plain singleton/factory, bypassing the
                Android ViewModel store — so it won't survive configuration changes and won't be
                cleared with its owner's lifecycle.
            """.trimIndent(),
            howToFix = "Use `by viewModel()` (or koinViewModel() in Compose) so the instance is lifecycle-scoped.",
            badExample = """
                private val viewModel: CountryListViewModel by inject()
            """.trimIndent(),
            goodExample = """
                private val viewModel: CountryListViewModel by viewModel()
            """.trimIndent(),
        )
    }
}
