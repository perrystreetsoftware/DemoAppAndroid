package com.example.konsist.logic

import com.example.konsist.Assertions.assertEmpty
import com.example.konsist.KonsistUtils.logicClassesProduction
import com.example.konsist.LintRuleMessage
import com.lemonappdev.konsist.api.ext.list.constructors
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import io.kotest.core.spec.style.BehaviorSpec

class LogicClassShouldInjectMaxFourReposOrLogic : BehaviorSpec() {

    init {
        Given("Any logic class in production") {
            val logicClasses = logicClassesProduction

            When("It defines a constructor") {
                val constructors = logicClasses.constructors

                Then("There are no constructors with more than 4 logic or repositories injected") {
                    constructors.filter {
                        val params = it.parameters.withNameEndingWith("Repository", "Logic")

                        return@filter params.count() > 4
                    }.assertEmpty(message = Message)
                }
            }
        }
    }

    private companion object {
        private val Message = LintRuleMessage(
            rule = "Do not inject more than 4 Repositories or Logic classes into a Logic class constructor.",
            why = """
                A Logic class exists to model one use case. Needing five or more collaborators
                means it is coordinating several use cases at once, which hides business rules
                and makes the class painful to test.
            """.trimIndent(),
            howToFix = "Extract cohesive groups of collaborators into intermediate Logic classes and compose them.",
            badExample = """
                class CheckoutLogic(
                    r1: CartRepository, r2: UserRepository, r3: PaymentRepository,
                    l1: PricingLogic, l2: ShippingLogic,
                )
            """.trimIndent(),
            goodExample = """
                class CheckoutLogic(
                    pricingLogic: PricingLogic,
                    paymentLogic: ProcessPaymentLogic,
                )
            """.trimIndent(),
        )
    }
}
