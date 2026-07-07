package com.example.konsist.enums

import com.example.konsist.Assertions.assertEmpty
import com.example.konsist.KonsistUtils.productionFiles
import com.example.konsist.LintRuleMessage
import io.kotest.core.spec.style.BehaviorSpec

class DoNotUseEnumOrdinal : BehaviorSpec() {

    init {
        Given("Production code") {
            Then("It does not use Enum.ordinal") {
                productionFiles
                    .filter { it.text.contains(".ordinal") }
                    .assertEmpty(message = Message)
            }
        }
    }

    private companion object {
        private val Message = LintRuleMessage(
            rule = "Do not use Enum.ordinal.",
            why = """
                .ordinal couples code to enum declaration order. Reordering cases (or
                inserting a new one) silently changes any persisted/transmitted ints,
                index-based lookups, and equality checks tied to ordinal — without a
                compile-time signal.
            """.trimIndent(),
            howToFix = """
                Give the enum an explicit stable property and use that instead of
                .ordinal. For ints already persisted to disk or sent over the wire, keep
                the existing numeric assignments to stay backward-compatible, and add a
                fromValue() helper for the reverse lookup.
            """.trimIndent(),
            badExample = """
                enum class Tab { A, B, C }
                prefs.putInt(KEY, tab.ordinal)
                val tab = Tab.entries[prefs.getInt(KEY, 0)]
            """.trimIndent(),
            goodExample = """
                enum class Tab(val value: Int) {
                    A(0), B(1), C(2);
                    companion object {
                        fun fromValue(value: Int): Tab? = entries.firstOrNull { it.value == value }
                    }
                }
                prefs.putInt(KEY, tab.value)
                val tab = Tab.fromValue(prefs.getInt(KEY, 0))
            """.trimIndent(),
        )
    }
}
