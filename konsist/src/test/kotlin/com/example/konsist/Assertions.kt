package com.example.konsist

import com.lemonappdev.konsist.api.provider.KoBaseProvider
import com.lemonappdev.konsist.api.provider.KoNameProvider
import com.lemonappdev.konsist.api.verify.assertEmpty
import com.lemonappdev.konsist.api.verify.assertFalse
import com.lemonappdev.konsist.api.verify.assertNotEmpty
import com.lemonappdev.konsist.api.verify.assertTrue

object Assertions {
    fun <E : KoBaseProvider> List<E?>.assertTrue(
        message: LintRuleMessage,
        strict: Boolean = false,
        testName: String? = null,
        baseline: Array<String> = emptyArray(),
        predicate: (E) -> Boolean?,
    ) {
        if (baseline.isEmpty()) {
            assertTrue(
                strict = strict,
                additionalMessage = message.formatted,
                testName = testName,
                function = predicate
            )
            return
        }

        val baselineSet = baseline.toSet()
        val normalItems = this.filterNot { it.matchesBaseline(baselineSet) }

        normalItems.assertTrue(
            strict = strict,
            additionalMessage = message.formatted,
            testName = testName,
            function = predicate
        )

        this.reportStaleBaseline(baselineSet, message) { predicate(it) == true }
    }

    fun <E : KoBaseProvider> List<E?>.assertFalse(
        message: LintRuleMessage,
        strict: Boolean = false,
        testName: String? = null,
        baseline: Array<String> = emptyArray(),
        predicate: (E) -> Boolean?,
    ) {
        if (baseline.isEmpty()) {
            assertFalse(
                strict = strict,
                additionalMessage = message.formatted,
                testName = testName,
                function = predicate
            )
            return
        }

        val baselineSet = baseline.toSet()
        val normalItems = this.filterNot { it.matchesBaseline(baselineSet) }

        normalItems.assertFalse(
            strict = strict,
            additionalMessage = message.formatted,
            testName = testName,
            function = predicate
        )

        this.reportStaleBaseline(baselineSet, message) { predicate(it) != true }
    }

    fun <E : KoBaseProvider> List<E?>.assertEmpty(
        message: LintRuleMessage,
        strict: Boolean = false,
        testName: String? = null,
        baseline: Array<String> = emptyArray(),
    ) {
        if (baseline.isEmpty()) {
            assertEmpty(strict = strict, additionalMessage = message.formatted, testName = testName)
            return
        }

        val baselineSet = baseline.toSet()
        val normalItems = this.filterNot { it.matchesBaseline(baselineSet) }

        normalItems.assertEmpty(strict = strict, additionalMessage = message.formatted, testName = testName)

        this.reportStaleBaseline(baselineSet, message)
    }

    fun <E : KoBaseProvider> List<E?>.assertNotEmpty(
        message: LintRuleMessage,
        strict: Boolean = false,
        testName: String? = null,
    ) {
        assertNotEmpty(strict = strict, additionalMessage = message.formatted, testName = testName)
    }

    fun <E : KoBaseProvider> E?.assertTrue(
        message: LintRuleMessage,
        strict: Boolean = false,
        testName: String? = null,
        predicate: (E) -> Boolean?,
    ) {
        assertTrue(strict = strict, additionalMessage = message.formatted, testName = testName, function = predicate)
    }

    fun <E : KoBaseProvider> E?.assertFalse(
        message: LintRuleMessage,
        strict: Boolean = false,
        testName: String? = null,
        predicate: (E) -> Boolean?,
    ) {
        assertFalse(strict = strict, additionalMessage = message.formatted, testName = testName, function = predicate)
    }

    private fun <E : KoBaseProvider> E?.matchesBaseline(baselineNames: Set<String>): Boolean {
        if (this == null) return false
        val name = (this as? KoNameProvider)?.name ?: return false
        return name in baselineNames
    }

    private fun <E : KoBaseProvider> List<E?>.reportStaleBaseline(
        baselineNames: Set<String>,
        message: LintRuleMessage,
        passesRule: ((E) -> Boolean)? = null,
    ) {
        val staleNames = if (passesRule != null) {
            this.filter { it.matchesBaseline(baselineNames) }.filterNotNull().groupBy { (it as KoNameProvider).name }
                .filter { (_, elements) -> elements.all { passesRule(it) } }.keys
        } else {
            emptySet()
        }

        val allNames = this.mapNotNull { (it as? KoNameProvider)?.name }.toSet()
        val unmatchedEntries = baselineNames.filter { it !in allNames }

        val allStale = (staleNames + unmatchedEntries).sorted()
        if (allStale.isNotEmpty()) {
            throw AssertionError(
                "Stale baseline: ${allStale.size} entry/entries no longer violate the rule and should be removed " +
                    "from the baseline:\n${allStale.joinToString(separator = ",\n") {
                        "\"$it\""
                    }}\n\n${message.formatted}"
            )
        }
    }
}
