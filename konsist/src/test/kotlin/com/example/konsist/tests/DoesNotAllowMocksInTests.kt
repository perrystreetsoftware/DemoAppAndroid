package com.example.konsist.tests

import com.example.konsist.Assertions.assertEmpty
import com.example.konsist.KonsistUtils
import com.example.konsist.LintRuleMessage
import com.lemonappdev.konsist.api.ext.list.withImport
import io.kotest.core.spec.style.BehaviorSpec

class DoesNotAllowMocksInTests : BehaviorSpec() {

    init {
        Given("I have a test file") {
            val testFiles = KonsistUtils.testCode.files

            Then("It should not have mocking library imports") {
                testFiles.withImport {
                    it.hasNameStartingWith("io.mockk") || it.hasNameStartingWith("org.mockito")
                }.assertEmpty(message = Message)
            }
        }
    }

    private companion object {
        private val Message = LintRuleMessage(
            rule = "Do not use mocking libraries in tests.",
            why = """
                Mocks couple tests to implementation details (which calls happen, in what order)
                instead of behavior, making refactors break tests that should still pass. Our
                tests wire the real Koin graph with a fake API layer, so behavior is tested
                end to end.
            """.trimIndent(),
            howToFix = "Use the fake API implementations (e.g. MockTravelApi bound by networkLogicApiMocks) and drive state through them.",
            badExample = """
                val api = mockk<ITravelAdvisoriesApi>()
                every { api.getCountryList() } returns Observable.just(dto)
            """.trimIndent(),
            goodExample = """
                // Koin binds MockTravelApi via networkLogicApiMocks;
                // configure its result and test the real pipeline.
            """.trimIndent(),
        )
    }
}
