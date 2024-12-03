package com.example.konsist.logic

import com.example.konsist.KonsistUtils
import com.lemonappdev.konsist.api.ext.list.modifierprovider.withPublicOrDefaultModifier
import com.lemonappdev.konsist.api.ext.list.withFunctions
import com.lemonappdev.konsist.api.ext.list.withProperty
import com.lemonappdev.konsist.api.ext.list.withoutFunction
import com.lemonappdev.konsist.api.ext.list.withoutName
import com.lemonappdev.konsist.api.verify.assertEmpty
import io.kotest.core.spec.style.BehaviorSpec

class LogicClassShouldOnlyExposeOneFunction : BehaviorSpec() {

    init {
        Given("A logic class") {
            val logicClasses = KonsistUtils.logicClassesProduction.withoutName(*baseline)

            Then("It exposes an invoke function") {
                logicClasses.withoutFunction {
                    it.name == "invoke" && it.hasPublicOrDefaultModifier && it.hasOperatorModifier
                }.assertEmpty(additionalMessage = MESSAGE_EXPOSE_INVOKE_FUNCTION)
            }

            Then("It does not expose more than one function") {
                logicClasses.withFunctions {
                    it.withPublicOrDefaultModifier().size > 1
                }.assertEmpty(
                    additionalMessage = MESSAGE_EXPOSE_ONLY_INVOKE_FUNCTION
                )
            }

            Then("It does not expose any property") {
                logicClasses.withProperty(includeNested = false) {
                    it.hasPublicOrDefaultModifier && !it.hasConstModifier
                }.assertEmpty(additionalMessage = MESSAGE_EXPOSE_NO_VARIABLES)
            }
        }
    }

    private companion object {
        private const val MESSAGE_EXPOSE_INVOKE_FUNCTION = "Logic classes should expose an operator invoke function"
        private const val MESSAGE_EXPOSE_ONLY_INVOKE_FUNCTION = "Logic classes should only expose one function"
        private const val MESSAGE_EXPOSE_NO_VARIABLES = "Logic classes should not expose any properties"

        private val baseline = arrayOf(
            "CountryListLogic",
            "ServerStatusLogic",
            "CountryDetailsLogic",
        )
    }
}
