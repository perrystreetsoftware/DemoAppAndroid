package com.example.konsist.tests

import com.example.konsist.Assertions.assertEmpty
import com.example.konsist.KonsistUtils
import com.example.konsist.LintRuleMessage
import com.lemonappdev.konsist.api.ext.list.classes
import com.lemonappdev.konsist.api.ext.list.modifierprovider.withoutAbstractModifier
import com.lemonappdev.konsist.api.ext.list.withParent
import com.lemonappdev.konsist.api.ext.list.withoutNameEndingWith
import io.kotest.core.spec.style.BehaviorSpec

class TestNameShouldEndWithTest : BehaviorSpec() {

    init {
        Given("I have a test class") {
            val testClasses = KonsistUtils.testCode.files
                .classes()
                .withoutAbstractModifier()
                .withoutNameEndingWith("Factory")
                .withParent(indirectParents = true) {
                    it.hasNameEndingWith("Spec") || it.hasNameEndingWith("Test")
                }

            Then("It should end with 'Test'") {
                testClasses.withoutNameEndingWith("Test")
                    .assertEmpty(message = Message, baseline = baseline)
            }
        }
    }

    private companion object {
        private val Message = LintRuleMessage(
            rule = "Test classes must end with 'Test'.",
            why = """
                A single suffix keeps test classes discoverable, lets tooling (Gradle --tests
                filters, scripts, lint scopes) match them reliably, and avoids the Test/Tests
                naming lottery when searching for a class's coverage.
            """.trimIndent(),
            howToFix = "Rename the class (and its file) to end with Test.",
            badExample = "class CountryListViewModelTests : KoinTest",
            goodExample = "class CountryListViewModelTest : KoinTest",
        )

        private val baseline = arrayOf(
            "BaseBehaviorSpec",
            "CountryDetailsLogicTests",
            "CountryDetailsRepositoryTests",
            "CountryDetailsViewModelTests",
            "CountryListLogicTests",
            "CountryListViewModelTests",
        )
    }
}
