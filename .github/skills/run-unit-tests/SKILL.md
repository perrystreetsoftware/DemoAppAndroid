---
name: run-unit-tests
description: Guide for running unit tests, interpreting BDD test output, and debugging test failures. Use this when asked to run tests, fix failing tests, debug test issues, or validate changes with tests.
---

# Running and Debugging Unit Tests

Tests are Kotest BehaviorSpec (Given/When/Then) or JUnit 5, running against the real Koin-wired classes with a fake API layer (`MockTravelApi`) — no mocking libraries.

## Running Tests

The test runner script filters Gradle noise and only prints pass/fail status with failure details.

**Execution:** Run the script in the terminal and **wait for it to complete**. Do **not** pipe the output through `tail`, `head`, or any other command. Do **not** run `./gradlew` directly. The script prints a clear `TESTS PASSED` or `TESTS FAILED` summary when done.

**Note:** Gradle in this project requires a JDK the wrapper supports (JDK 17 works). If the default JDK fails with "Unsupported class file major version", export `JAVA_HOME` pointing at a JDK 17 installation first.

```bash
# Specific test class (module is auto-detected)
bash .github/skills/run-unit-tests/run-tests.sh CountryDetailsViewModelBehaviorTest

# Pattern match (module is auto-detected)
bash .github/skills/run-unit-tests/run-tests.sh "*CountryList*"

# Multiple test classes — use --module since all must be in the same module
bash .github/skills/run-unit-tests/run-tests.sh --module viewmodels CountryListViewModelTests CountryDetailsViewModelTests

# All tests in a specific module
bash .github/skills/run-unit-tests/run-tests.sh --module logic

# All unit tests (excludes Konsist lint rules)
bash .github/skills/run-unit-tests/run-tests.sh
```

### Available modules

| Alias | Gradle module | Notes |
|---|---|---|
| `viewmodels` | `viewmodels` | ViewModel tests (BDD) |
| `logic` | `logic` | Logic tests |
| `repositories` (or `repo`) | `repositories` | Repository tests |
| `interfaces` (or `dto`) | `interfaces` | DTO parsing tests |
| `networklogic` | `networklogic` | Network layer tests |
| `feature` | `feature` | UI-layer JVM tests |
| `utils` | `utils` | Utility tests |
| `app` | `app` | App module tests |

## Which Tests to Run

Run only the tests affected by your changes — do not run all tests unless explicitly asked or you changed files across multiple modules and are unsure which tests are affected.

1. **Changed a ViewModel** → run its test class
2. **Changed a Logic class** → run its Logic test plus the ViewModel tests that inject it
3. **Changed a Repository** → run its test plus the Logic/ViewModel tests upstream of it
4. **Changed a DTO or the API layer** → run the `interfaces` module tests plus affected repositories
5. **Unsure or asked for all** → run the script with no arguments

## Interpreting Failures

- The script extracts the failure message and the Given/When/Then path of each failing test from the JUnit XML reports.
- For BDD tests, the failing `Then` name tells you the exact scenario; remember each `Then` runs in an isolated instance (`InstancePerLeaf`), so a failure cannot be caused by a sibling `Then`.
- If the failure is a Koin error (`NoBeanDefFoundException` etc.), a module definition is missing — check the modules loaded in `BaseBehaviorSpec` or the test's `startKoin` block.
- After fixing, re-run the failing class, then run the module's tests before declaring done.
- Konsist lint rules are separate — run them with the `run-konsist` skill.
