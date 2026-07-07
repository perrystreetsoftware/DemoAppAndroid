package com.example.konsist.logic

import com.example.konsist.Assertions.assertEmpty
import com.example.konsist.KonsistUtils
import com.example.konsist.LintRuleMessage
import com.lemonappdev.konsist.api.ext.list.modifierprovider.withPublicOrDefaultModifier
import com.lemonappdev.konsist.api.ext.list.withFunctions
import com.lemonappdev.konsist.api.ext.list.withProperty
import com.lemonappdev.konsist.api.ext.list.withoutFunction
import io.kotest.core.spec.style.BehaviorSpec

class LogicClassShouldOnlyExposeOneFunction : BehaviorSpec() {

    init {
        Given("A logic class") {
            val logicClasses = KonsistUtils.logicClassesProduction

            Then("It exposes an invoke function") {
                logicClasses.withoutFunction {
                    it.name == "invoke" && it.hasPublicOrDefaultModifier && it.hasOperatorModifier
                }.assertEmpty(message = InvokeMessage, baseline = baselineNoInvoke)
            }

            Then("It does not expose more than one function") {
                logicClasses.withFunctions {
                    it.withPublicOrDefaultModifier().size > 1
                }.assertEmpty(message = SingleFunctionMessage, baseline = baselineMultipleFunctions)
            }

            Then("It does not expose any property") {
                logicClasses.withProperty(includeNested = false) {
                    it.hasPublicOrDefaultModifier && !it.hasConstModifier
                }.assertEmpty(message = NoPropertiesMessage, baseline = baselineExposesProperties)
            }
        }
    }

    private companion object {
        private val InvokeMessage = LintRuleMessage(
            rule = "Logic classes must expose an operator fun invoke().",
            why = """
                Logic classes follow the single-responsibility principle: one class, one use case.
                A uniform invoke() entry point makes every Logic class interchangeable and trivially
                discoverable.
            """.trimIndent(),
            howToFix = "Rename the public function to operator fun invoke().",
            badExample = "class GetCountryLogic { fun execute(): Single<Country> { ... } }",
            goodExample = "class GetCountryLogic { operator fun invoke(): Single<Country> { ... } }",
        )

        private val SingleFunctionMessage = LintRuleMessage(
            rule = "Logic classes must expose only one function.",
            why = """
                A Logic class with several public functions is several use cases in disguise, which
                blurs its responsibility and makes callers depend on more than they need.
            """.trimIndent(),
            howToFix = "Split each extra public function into its own Logic class.",
            badExample = """
                class CountryLogic {
                    operator fun invoke(): Single<Country> { ... }
                    fun reload(): Completable { ... }
                }
            """.trimIndent(),
            goodExample = """
                class GetCountryLogic { operator fun invoke(): Single<Country> { ... } }
                class ReloadCountryLogic { operator fun invoke(): Completable { ... } }
            """.trimIndent(),
        )

        private val NoPropertiesMessage = LintRuleMessage(
            rule = "Logic classes must not expose public properties.",
            why = """
                Exposed properties are hidden entry points that bypass the single invoke()
                contract and leak state out of the domain layer.
            """.trimIndent(),
            howToFix = "Return the value from invoke() or move the stream into its own Logic class.",
            badExample = """
                class CountryLogic(repository: ICountryRepository) {
                    val countries: Observable<List<Country>> = repository.countries
                }
            """.trimIndent(),
            goodExample = """
                class GetCountriesLogic(private val repository: ICountryRepository) {
                    operator fun invoke(): Observable<List<Country>> = repository.countries
                }
            """.trimIndent(),
        )

        private val baselineNoInvoke = arrayOf(
            "CountryListLogic",
            "ServerStatusLogic",
            "CountryDetailsLogic",
        )

        private val baselineMultipleFunctions = arrayOf(
            "CountryListLogic",
        )

        private val baselineExposesProperties = arrayOf(
            "CountryListLogic",
            "ServerStatusLogic",
        )
    }
}
