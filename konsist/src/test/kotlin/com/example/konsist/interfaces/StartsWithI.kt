package com.example.konsist.interfaces

import com.example.konsist.Assertions.assertTrue
import com.example.konsist.KonsistUtils.interfacesProduction
import com.example.konsist.LintRuleMessage
import io.kotest.core.spec.style.BehaviorSpec

class StartsWithI : BehaviorSpec() {

    init {
        Given("An interface") {
            val interfaces = interfacesProduction

            Then("It starts with I") {
                interfaces.assertTrue(message = Message, baseline = baseline) {
                    it.hasNameStartingWith("I")
                }
            }
        }
    }

    private companion object {
        private val Message = LintRuleMessage(
            rule = "Interfaces must be prefixed with I.",
            why = """
                The I prefix makes it obvious at the call site whether a dependency is an
                abstraction or a concrete class, and frees the un-prefixed name for the default
                implementation.
            """.trimIndent(),
            howToFix = "Rename the interface to start with I and keep the un-prefixed name for the implementation.",
            badExample = "interface CountryRepository",
            goodExample = "interface ICountryRepository",
        )

        private val baseline = arrayOf(
            "CustomErrorStateFactory",
        )
    }
}
