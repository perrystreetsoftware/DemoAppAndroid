package com.example.konsist.styling

import com.example.konsist.Assertions.assertEmpty
import com.example.konsist.KonsistUtils
import com.example.konsist.LintRuleMessage
import com.lemonappdev.konsist.api.ext.list.withText
import io.kotest.core.spec.style.BehaviorSpec

class DoNotUseRegionComments : BehaviorSpec() {

    init {
        Given("I have production files") {
            val productionFiles = KonsistUtils.productionFiles

            When("I check for region comments") {
                val filesWithRegions = productionFiles.withText { text ->
                    regionRegex.containsMatchIn(text)
                }

                Then("No files should contain region comments") {
                    filesWithRegions.assertEmpty(message = Message)
                }
            }
        }
    }

    private companion object {
        private val regionRegex = Regex("""//\s*(region|endregion)""")

        private val Message = LintRuleMessage(
            rule = "Do not use region comments (// region / // endregion).",
            why = """
                Regions hide complexity behind collapsible blocks instead of removing it. A file
                that needs regions is doing too much and violates the Single Responsibility
                Principle.
            """.trimIndent(),
            howToFix = "Extract each region's responsibility into its own class or file instead of grouping with comments.",
            badExample = """
                // region networking
                fun load() { ... }
                // endregion
            """.trimIndent(),
            goodExample = """
                class CountryLoader { fun load() { ... } }
            """.trimIndent(),
        )
    }
}
