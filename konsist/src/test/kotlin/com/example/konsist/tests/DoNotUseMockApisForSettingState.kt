package com.example.konsist.tests

import com.example.konsist.Assertions.assertFalse
import com.example.konsist.KonsistUtils.testCode
import com.example.konsist.LintRuleMessage
import io.kotest.core.spec.style.BehaviorSpec

class DoNotUseMockApisForSettingState : BehaviorSpec() {

    init {
        Given("A test class") {
            val testClasses = testCode.classes(includeNested = false)

            Then("It does not set state in a mock API") {
                testClasses.assertFalse(message = Message) {
                    it.text.contains(settingStateInApiRegex)
                }
            }
        }
    }

    private val settingStateInApiRegex = Regex("(\\w+)?(?i:api)\\.\\w+\\s=\\s")

    private companion object {
        private val Message = LintRuleMessage(
            rule = "Tests must not set state on mock APIs directly.",
            why = """
                Reaching into a mock API couples the test to the API's internals instead of the
                behavior under test. Test factories give one shared, intention-revealing way to
                arrange state.
            """.trimIndent(),
            howToFix = "Use (or create) a test factory that builds the state you need and pass it to the system under test.",
            badExample = """
                mockApi.serverStatus = ServerStatusDTO(ok = false)
            """.trimIndent(),
            goodExample = """
                val repository = ServerStatusRepositoryFactory.build(status = ServerStatus.Down)
            """.trimIndent(),
        )
    }
}
