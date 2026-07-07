---
name: run-konsist
description: Runs Konsist architectural lint rules and guides fixing violations. Use after implementing code to verify it follows the project's architectural conventions.
---

You are running Konsist, a set of architectural lint rules that enforce coding conventions across the codebase. These rules are implemented as Kotest BehaviorSpec tests in `konsist/src/test/kotlin/com/example/konsist/`.

## How to Run

Use the helper script to run rules and get filtered output (only failures are shown):

```bash
# Run ALL lint rules
bash .github/skills/run-konsist/run-konsist.sh

# Run a specific rule
bash .github/skills/run-konsist/run-konsist.sh DoesNotHaveInitializer

# Run multiple specific rules
bash .github/skills/run-konsist/run-konsist.sh DoesNotHaveInitializer StartsWithI
```

The rule name matches the Kotlin class name of the test (e.g., `DoesNotDependOnRepositories`).

**Important:** Always use the helper script instead of calling `./gradlew` directly. It filters task output down to only actionable failure information.

**Execution:** Run the script in the terminal and **wait for it to complete** — it may take several minutes. Do **not** pipe the output through `tail`, `head`, or any other command. Do **not** run `./gradlew` directly. The script will print a clear `KONSIST LINT RULES PASSED` or `KONSIST LINT RULES FAILED` summary when done.

**Note:** Gradle in this project requires a JDK the wrapper supports (JDK 17 works). If the default JDK fails with "Unsupported class file major version", export `JAVA_HOME` pointing at a JDK 17 installation first.

## Interpreting Results

- **All rules pass:** The script prints `KONSIST LINT RULES PASSED`.
- **Rules fail:** The script prints each failing rule name and its violation details (which classes/files violated, with file paths and line numbers).
- **Stale baseline:** A failure message starting with `Stale baseline:` means entries in the rule's `baseline` array (or concatenated allowed list) are no longer needed — either the violation was fixed or the element no longer exists. Remove the listed entries from the `baseline` array or allowed list in the rule's companion object.
- **Build failure:** If the code doesn't compile, fix compilation errors first, then re-run.

## Fixing Violations

For each failing rule:

1. **Read the rule's test file** to understand what it enforces. All rules are located at:
   ```
   konsist/src/test/kotlin/com/example/konsist/<category>/<RuleName>.kt
   ```

2. **Read the rule's message carefully** — it explains the architectural reasoning and how to fix the violation:
   - Rules use `LintRuleMessage` (a data class with structured fields: `rule`, `why`, `howToFix`, `badExample`, `goodExample`). The violation output will show all these fields formatted with `RULE:`, `WHY:`, `HOW TO FIX:`, `❌ BAD:`, and `✅ GOOD:` sections. Read and follow the `howToFix` guidance and use the `goodExample` as the target pattern.
   - The message contains the architectural reasoning for WHY the rule exists. Understanding this reasoning is essential to applying a correct fix.

3. **Fix the violation in production code**, not in the test file. The violation output references which file or class is violating the rule.

4. **Re-run just that rule** to verify the fix:
   ```bash
   bash .github/skills/run-konsist/run-konsist.sh <RuleName>
   ```

## What Is Not A Valid Fix

When fixing Konsist violations, do not bypass the rule checks. These are considered invalid unless the user explicitly asks to change lint policy:

- Do not add files to `baseline` / `allowed` arrays in Konsist tests to silence a violation.
- Do not inline or fully qualify imports (for example replacing an import with `com.foo.Bar`) just to evade import-based rules.
- Do not weaken or modify a Konsist rule just to make current code pass.

Always apply a root-cause fix in production code that preserves architecture intent (for example: replace forbidden dependencies with the correct layer, move logic to the correct layer, or introduce the missing abstraction).

## Pre-Completion Sanity Check

Before declaring lint issues fixed:

1. Confirm the changed files are implementation files, not primarily Konsist test files.
2. If any file under `konsist/src/test/` was changed, explain why and confirm the user asked for lint policy changes.
3. Re-run the failing rule(s) and ensure they pass without relying on allowlist/baseline edits.
4. After fixing targeted rule failures, run the full Konsist suite before declaring the work done. Do not stop after the first green targeted rule because another rule may still fail.

## Rule Categories

Rules are organized in `konsist/src/test/kotlin/com/example/konsist/`. Examples of what they check:

| Category | Directory | What it checks |
|---|---|---|
| ViewModel | `viewmodel/` | LifecycleViewModel base class, no init blocks, no repo dependencies, state conventions, disposables handling, max 6 Logic injections |
| Logic | `logic/` | Single `invoke()`, no exposed properties, max 4 repos/logic injected |
| Interfaces | `interfaces/` | `I` prefix naming |
| UI | `ui/` | No `mutableStateOf` retained in Composables |
| Enums | `enums/` | No `Enum.ordinal` usage |
| Styling | `styling/` | File layout conventions |
| Tests | `tests/` | No mock APIs for state, meta-rules for Konsist itself |
| RxJava | `libraries/rxjava/` | BehaviorSubjects must have default values |
