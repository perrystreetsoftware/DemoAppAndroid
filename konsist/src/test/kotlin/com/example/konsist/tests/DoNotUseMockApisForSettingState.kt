package com.example.konsist.tests

import com.example.konsist.KonsistUtils.testCode
import com.lemonappdev.konsist.api.verify.assertFalse
import io.kotest.core.spec.style.BehaviorSpec

class DoNotUseMockApisForSettingState : BehaviorSpec() {

    init {
        Given("A test class") {
            val testClasses = testCode.classes(includeNested = false)

            Then("It does not set state in a mock API") {
                testClasses.assertFalse(additionalMessage = MESSAGE) {
                    it.text.contains(settingStateInApiRegex)
                }
            }
        }
    }

    private val settingStateInApiRegex = Regex("(\\w+)?(?i:api)\\.\\w+\\s=\\s")

    private companion object {
        private const val MESSAGE = "Use test factories to set state, do not use mock APIs directly"
    }
}
