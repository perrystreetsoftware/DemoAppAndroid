package com.example.konsist.viewmodel

import com.example.konsist.Assertions.assertFalse
import com.example.konsist.KonsistUtils.viewModelsProduction
import com.example.konsist.LintRuleMessage
import com.lemonappdev.konsist.api.ext.list.classes
import com.lemonappdev.konsist.api.ext.list.modifierprovider.withSealedModifier
import com.lemonappdev.konsist.api.ext.list.withNameContaining
import io.kotest.core.spec.style.BehaviorSpec

class DoesNotExposeErrorEvents : BehaviorSpec() {

    init {
        Given("A ViewModel") {
            val viewModels = viewModelsProduction

            When("It has an Event sealed class") {
                val events = viewModels.classes().withSealedModifier().withNameContaining("Event")

                Then("It does not have an Error class") {
                    events.assertFalse(message = Message) { event ->
                        event.hasClass { it.name.contains("Error") }
                    }
                }

                Then("It does not have an Error object") {
                    events.assertFalse(message = Message) { event ->
                        event.hasObject { it.name.contains("Error") }
                    }
                }
            }
        }
    }

    private companion object {
        private val Message = LintRuleMessage(
            rule = "ViewModels must not expose errors as events.",
            why = """
                Errors modeled as one-shot events get lost on configuration changes and cannot be
                re-rendered by the view. Errors are state, and our LifecycleViewModel already
                exposes an error observable for exactly that.
            """.trimIndent(),
            howToFix = "Emit the error through LifecycleViewModel's error observable (mutableError) instead of an Event subclass.",
            badExample = """
                sealed class Event {
                    object LoadError : Event()
                }
            """.trimIndent(),
            goodExample = """
                // Inside the ViewModel, using LifecycleViewModel's error observable:
                mutableError.onNext(Optional.of(error))
            """.trimIndent(),
        )
    }
}
