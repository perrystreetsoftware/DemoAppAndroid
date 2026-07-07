package com.example.konsist

/**
 * Take this snippet, below, when converting from legacy messages to the new style
 *
 *     private val message = LintRuleMessage(
 *         rule = "",
 *         why = "",
 *         howToFix = "",
 *         badExample = "",
 *         goodExample = "",
 *     )
 *
 **/

data class LintRuleMessage(
    val rule: String,
    val why: String,
    val howToFix: String,
    val badExample: String,
    val goodExample: String,
) {
    val formatted: String
        get() = buildString {
            appendLine("RULE: $rule")
            appendLine()
            appendLine("WHY: $why")
            appendLine()
            appendLine("HOW TO FIX: $howToFix")
            appendLine()
            appendLine("❌ BAD:")
            appendLine(badExample)
            appendLine()
            appendLine("✅ GOOD:")
            appendLine(goodExample)
        }
}
