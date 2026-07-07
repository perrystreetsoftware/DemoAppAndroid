package com.example.konsist.tests

import com.example.konsist.Assertions.assertFalse
import com.example.konsist.KonsistUtils
import com.example.konsist.LintRuleMessage
import io.kotest.core.spec.style.BehaviorSpec

class DoNotUseSafeCallOnAssertions : BehaviorSpec() {

    init {
        Given("A test file") {
            val testFiles = KonsistUtils.testCode.files

            Then("It must not use safe-call operators on assertion functions") {
                testFiles.assertFalse(message = Message) {
                    it.text.contains("?.should")
                }
            }
        }
    }

    private companion object {
        private val Message = LintRuleMessage(
            rule = "Do not use safe-call operator (?.) on assertion functions.",
            why = """
                Using `?.shouldBeTrue()`, `?.shouldBeEqual(x)`, or similar patterns silently
                skips the assertion when the receiver is null. The test passes without actually
                verifying anything, hiding real bugs.
            """.trimIndent(),
            howToFix = """
                Use infix-style assertions from Kluent that properly handle null comparisons:
                - `x?.shouldBeTrue()`     → `x shouldBeEqualTo true`
                - `x?.shouldBeEqual(y)`   → `x shouldBeEqualTo y`
                - `x?.shouldBeNull()`     → `x shouldBe null`
            """.trimIndent(),
            badExample = """
                state.loaded?.showIndicator?.shouldBeTrue()
            """.trimIndent(),
            goodExample = """
                state.loaded?.showIndicator shouldBeEqualTo true
            """.trimIndent(),
        )
    }
}
