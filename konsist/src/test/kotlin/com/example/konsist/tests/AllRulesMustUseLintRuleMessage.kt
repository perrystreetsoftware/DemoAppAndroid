package com.example.konsist.tests

import com.example.konsist.Assertions.assertTrue
import com.example.konsist.KonsistUtils
import com.example.konsist.LintRuleMessage
import io.kotest.core.spec.style.BehaviorSpec

class AllRulesMustUseLintRuleMessage : BehaviorSpec() {

    init {
        Given("A Konsist lint rule file") {
            val konsistRuleFiles = KonsistUtils.konsistRulesFiles

            Then("It imports LintRuleMessage") {
                konsistRuleFiles.assertTrue(message = Message, baseline = nonRuleFiles) {
                    it.hasImport { import -> import.name == LintRuleMessageImport }
                }
            }
        }
    }

    private companion object {
        private const val LintRuleMessageImport = "com.example.konsist.LintRuleMessage"

        private val Message = LintRuleMessage(
            rule = "All Konsist lint rules must use LintRuleMessage for assertion messages.",
            why = """
                Structured messages ensure every rule explains WHY it exists, HOW TO FIX violations,
                and provides good/bad examples. This prevents AI agents from bypassing rules
                because they don't understand the architectural reasoning behind them.
            """.trimIndent(),
            howToFix = """
                Replace raw String messages with LintRuleMessage instances and use
                the assertEmpty/assertTrue/assertFalse overloads from com.example.konsist.Assertions
                that accept a LintRuleMessage parameter.
            """.trimIndent(),
            badExample = """
                private const val Message = "Logic classes should expose operator invoke function"

                violations.assertEmpty(additionalMessage = Message)
            """.trimIndent(),
            goodExample = """
                private val Message = LintRuleMessage(
                    rule = "Logic classes must expose operator fun invoke().",
                    why = "Logic classes follow SRP — one class, one use case.",
                    howToFix = "Rename your public function to operator fun invoke().",
                    badExample = "class MyLogic { fun execute() { ... } }",
                    goodExample = "class MyLogic { operator fun invoke() { ... } }",
                )

                violations.assertEmpty(message = Message)
            """.trimIndent(),
        )

        // Infrastructure files in the konsist module that are not lint rules.
        private val nonRuleFiles = arrayOf(
            "Assertions",
            "KonsistUtils",
            "LintRuleMessage",
        )
    }
}
