---
name: write-konsist-rule
description: Guide for creating new Konsist lint rules that enforce architectural patterns and best practices. Use this when asked to write a lint rule or enforce an architectural pattern.
---

# Writing Konsist Rules

Konsist is a Kotlin static-analysis library that lets us write architectural and structural lint rules as unit tests. Rules are Kotest BehaviorSpec tests in `konsist/src/test/kotlin/com/example/konsist/`. Subfolders group rules by category. Each file contains a **single lint rule** as a BehaviorSpec test class that fails when the rule is violated.

## Rule Template

```kotlin
package com.example.konsist.<category>

import com.example.konsist.Assertions.assertTrue  // or assertEmpty, assertFalse from com.example.konsist.Assertions
import com.example.konsist.KonsistUtils
import com.example.konsist.LintRuleMessage
import com.lemonappdev.konsist.api.ext.list...
import io.kotest.core.spec.style.BehaviorSpec

class MyRuleName : BehaviorSpec() {
    init {
        Given("A descriptive context for the rule") {
            val items = KonsistUtils.someScope

            When("A condition is checked") {   // When is optional
                Then("The expected property holds") {
                    items.assertTrue(message = Message, baseline = baseline) {
                        // predicate
                    }
                }
            }
        }
    }

    private companion object {
        private val Message = LintRuleMessage(
            rule = "Short description of what the rule checks.",
            why = """
                Architectural reasoning — why does this rule exist?
                What problem does it prevent?
            """.trimIndent(),
            howToFix = "Concrete steps to fix a violation.",
            badExample = """
                class BadExample {
                    // code that violates the rule
                }
            """.trimIndent(),
            goodExample = """
                class GoodExample {
                    // code that follows the rule
                }
            """.trimIndent(),
        )

        private val baseline = arrayOf(
            "KnownViolation1",
            "KnownViolation2",
        )
    }
}
```

### LintRuleMessage

All new rules **must** use `LintRuleMessage` for assertion messages. This is enforced by the `AllRulesMustUseLintRuleMessage` meta-lint rule.

`LintRuleMessage` is a data class at `konsist/src/test/kotlin/com/example/konsist/LintRuleMessage.kt` with the following required fields:

| Field | Purpose |
|---|---|
| `rule` | Short description of what the rule checks |
| `why` | Architectural reasoning — why does this rule exist? What problem does it prevent? |
| `howToFix` | Concrete steps to fix a violation |
| `badExample` | Code example that violates the rule |
| `goodExample` | Code example that follows the rule |

Wrapper extension functions for `assertTrue`, `assertFalse`, `assertEmpty`, and `assertNotEmpty` are defined in `konsist/src/test/kotlin/com/example/konsist/Assertions.kt`. These accept `message: LintRuleMessage` instead of `additionalMessage: String` and are imported from `com.example.konsist.Assertions`.

Use triple-quoted strings (`"""...""".trimIndent()`) for any field value that spans multiple lines. Never use `+` to concatenate strings across lines.

When a rule has **multiple Then blocks with different assertions**, create a separate `LintRuleMessage` for each assertion. The `why` field can be the same across messages if they share the same architectural reasoning.

If the rule has **existing violations that cannot be fixed immediately**, add a baseline to the companion object and pass it to the assertion via the `baseline` parameter. The assertion will automatically exclude baselined elements and detect stale entries (violations that have been fixed but not removed from the baseline). Never add an empty baseline — only include it when there are actual violations to exclude:

```kotlin
// In the companion object, list only actual violations:
private val baseline = arrayOf(
    "KnownViolation1",
    "KnownViolation2",
)
```

```kotlin
// In the Then block, pass baseline to the assertion:
items.assertTrue(message = Message, baseline = baseline) {
    // predicate
}

// For assertFalse:
items.assertFalse(message = Message, baseline = baseline) {
    // predicate
}

// For assertEmpty:
violations.assertEmpty(message = Message, baseline = baseline)
```

**Do NOT pre-filter baselines with `.withoutName(*baseline)`** — always pass the baseline to the assertion. The assertion handles exclusion, stale detection (entries that no longer violate), and unmatched detection (entries that no longer exist).

When a rule has multiple assertions, keep a **separate baseline per assertion** listing only the elements that violate that specific assertion — an element that passes an assertion but sits in its baseline is reported as stale.

Rules without any baseline should omit the parameter - it defaults to `emptyArray()`.

## Key Conventions

- Always extend `BehaviorSpec()` with `Given`/`When`/`Then` blocks.
- Always use `LintRuleMessage` for assertion messages — never use raw strings.
- Import assertion functions from `com.example.konsist.Assertions` (not from `com.lemonappdev.konsist.api.verify`).
- Never call `Konsist.scopeFromDirectory(...)` directly — use `KonsistUtils` scopes.
- Baselines use `arrayOf(...)` in `companion object` and are passed to the assertion via `baseline = baseline`. Do not pre-filter with `.withoutName(*baseline)`.
- Define `LintRuleMessage` instances in `companion object` as `private val`.

## Konsist API Reference

All Konsist APIs are imported from `com.lemonappdev.konsist.api.*`.

### Scopes

Scopes define which source files a rule applies to. Defined in `KonsistUtils` (`konsist/src/test/kotlin/com/example/konsist/KonsistUtils.kt`).

**Always use pre-defined scopes from `KonsistUtils`:**

| Scope | What it covers |
|---|---|
| `productionCode` | All production source |
| `testCode` | All test source, excluding Konsist tests |
| `konsistRulesFiles` | The Konsist rule files themselves (for meta-rules) |

### Pre-filtered Entity Lists

| Property | Returns |
|---|---|
| `viewModelsProduction` | Classes ending with `ViewModel` |
| `logicClassesProduction` | Classes ending with `Logic` |
| `mapperObjectsProduction` | Objects ending with `Mapper` |
| `repositoryClassesProduction` | Classes ending with `Repository` |
| `apiClassesProduction` | Classes ending with `Api` |
| `interfacesProduction` | All production interfaces |
| `classesProduction` | All production classes |
| `productionFiles` | All production files |
| `composableFunctions` | Functions annotated with `@Composable` |

### AST Navigation

From a scope:
- `scope.files` — all files
- `scope.classes()` — all classes
- `scope.interfaces()` — all interfaces
- `scope.objects()` — all objects
- `scope.functions()` — all top-level functions

From entities:
- `class.properties()` / `class.functions()` / `class.constructors`
- `file.imports` — import declarations
- `class.parents()` — parent classes/interfaces

### Filtering

Use Konsist's built-in filtering methods instead of `.filter { }`. **Avoid regular expressions** unless there is no Konsist API that covers the check — the built-in methods are more readable and maintainable:

**Name-based:**
```kotlin
.withName("ExactName")
.withNameEndingWith("ViewModel")
.withoutNameEndingWith("JsonMapper")
.withNameStartingWith("Get")
.withNameContaining("DTOToDomain")
.withNameMatching(Regex(".*State$"))
```

**Modifier-based:**
```kotlin
.withPublicOrDefaultModifier()
.withPrivateModifier()
.withSealedModifier()
.withDataModifier()
.withOperatorModifier()
.withAbstractModifier()
```

**Relationship-based:**
```kotlin
.withParent { it.name == "LifecycleViewModel" }
.withParentNamed("Throwable")
.withAnnotationNamed("Composable", "Preview")
.withFunction { it.name == "invoke" }
.withProperty { it.hasPublicOrDefaultModifier }
.withImport { it.hasNameStartingWith("com.example.repositories") }
```

**Compound:**
```kotlin
.withFunctions { it.withPublicOrDefaultModifier().size > 1 }
.withoutFunction { it.name == "invoke" && it.hasOperatorModifier }
```

### Assertions

Use the `LintRuleMessage` overloads from `com.example.konsist.Assertions`. If there is a baseline, pass it as the `baseline` parameter in `assertTrue`, `assertFalse`, and `assertEmpty`. If there is no baseline, omit the parameter.

| Assertion | Purpose |
|---|---|
| `list.assertTrue(message, baseline = baseline) { predicate }` | Every item must satisfy the predicate |
| `list.assertFalse(message, baseline = baseline) { predicate }` | No item may satisfy the predicate |
| `list.assertEmpty(message, baseline = baseline)` | List must be empty (use after filtering to violations) |
| `list.assertTrue(message, strict = true, baseline = baseline) { predicate }` | Like assertTrue, but also fails on empty list |

## Steps

1. **Create the rule** following the template. One rule per file. Place it in the most logical category folder.
  - **Note:** This skill covers the most commonly used patterns. If you need an API not listed here, first study existing rules in `konsist/src/test/kotlin/com/example/konsist/` — they are the ground truth for what the project uses. For the full API, refer to the [Konsist GitHub repo](https://github.com/LemonAppDev/konsist).
2. **Run the rule** using the `run-konsist` skill
3. **Handle violations** `baseline` = violations to fix later. `allowed` = intentionally permitted exceptions. Never create empty baseline or allowed arrays — omit them if there are none.
4. **Re-run** to confirm it passes.
