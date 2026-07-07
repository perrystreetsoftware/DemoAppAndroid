package com.example.konsist.tests

import com.example.konsist.Assertions.assertFalse
import com.example.konsist.KonsistUtils
import com.example.konsist.LintRuleMessage
import io.kotest.core.spec.style.BehaviorSpec

class DoNotUseWithoutNameForBaseline : BehaviorSpec() {

    init {
        Given("A Konsist lint rule file") {
            val konsistRuleFiles = KonsistUtils.konsistRulesFiles

            Then("It is using our baseline API instead of withoutName") {
                konsistRuleFiles.assertFalse(message = Message, baseline = baseline) {
                    it.text.contains("withoutName(baseline") ||
                        it.text.contains("withoutName(*baseline") ||
                        it.text.contains("withoutName(allowed") ||
                        it.text.contains("withoutName(*allowed")
                }
            }
        }
    }

    private companion object {
        private val Message = LintRuleMessage(
            rule = "Konsist lint rules must use the baseline parameter instead of withoutName.",
            why = """
                Using .withoutName(*baseline) to exclude known violations bypasses our stale
                baseline detection. The baseline parameter on assertTrue/assertFalse/assertEmpty
                automatically detects when entries no longer violate the rule and prompts to remove them.
            """.trimIndent(),
            howToFix = """
                Remove the .withoutName(*baseline) call and pass the baseline array directly
                to the assertion's baseline parameter instead.
            """.trimIndent(),
            badExample = """
                val classes = scope.withoutName(*baseline)
                classes.assertTrue(message = Message) { ... }
            """.trimIndent(),
            goodExample = """
                val classes = scope
                classes.assertTrue(message = Message, baseline = baseline) { ... }
            """.trimIndent(),
        )

        // This rule's own file contains the forbidden strings as detection literals.
        private val baseline = arrayOf(
            "DoNotUseWithoutNameForBaseline",
        )
    }
}
