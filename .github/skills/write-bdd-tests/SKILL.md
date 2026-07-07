---
name: write-bdd-tests
description: Guide for writing BDD-style unit tests using Kotest BehaviorSpec. Use this when asked to write tests, create a ViewModel test, fix a failing test, or implement Given/When/Then tests.
---

# Writing BDD Tests with Kotest BehaviorSpec

New tests in this project use Kotest's `BehaviorSpec` with Given/When/Then structure. ViewModel tests extend `BaseBehaviorSpec` (`viewmodels/src/test/kotlin/com/example/viewmodels/spec/BaseBehaviorSpec.kt`), which:

- Runs with `IsolationMode.InstancePerLeaf` — each `Then` block gets a fresh instance with all ancestor setup replayed.
- Starts Koin with the real production modules plus mock APIs: `viewModelModule + logicModule + repositoriesModule + networkLogicApiMocks`.
- Replaces RxJava's io/newThread/single schedulers with `Schedulers.trampoline()` via `RxSchedulersSpecExtension`, so streams execute synchronously.

Older tests in `logic/`, `repositories/`, and `interfaces/` use plain JUnit 5 with `@Nested`/`@DisplayName`. Follow the BDD style for new tests; don't rewrite old ones unless asked.

## Key Principles

1. **No mocking libraries** — the DI graph is real; only the API layer is faked (`MockTravelApi` bound by `networkLogicApiMocks`)
2. **Do not set state on mock APIs directly in tests** — enforced by the `DoNotUseMockApisForSettingState` Konsist rule. Configure responses through a test factory (create one if the scenario needs it)
3. **Only `BehaviorSpec`** (Given/When/Then) for new tests — no `DescribeSpec`
4. **Assertions with Kluent** — `shouldBeEqualTo`, `shouldBeInstanceOf`
5. **Observe reactive state with `.test()`** RxJava test observers

## Example ViewModel Test

```kotlin
class CountryDetailsViewModelBehaviorTest : BaseBehaviorSpec() {

    private val viewModel: CountryDetailsViewModel by inject {
        parametersOf("ug")
    }

    init {
        Given("I tap on a country") {
            val testObserver = viewModel.state.test()

            Then("The page is loaded") {
                testObserver.values()
                    .last() shouldBeInstanceOf CountryDetailsViewModel.UiState.Loaded::class
            }
        }
    }
}
```

## Structure Rules

### Given — Sets up the initial context
- Describes the starting scenario (e.g., "I open the country list screen")
- Subscribe test observers here (`viewModel.state.test()`)
- Use `beforeEach` for actions that must run before every leaf

### When — Describes an action or event
- Configure API responses (via factories) **outside** `beforeEach`
- Trigger the user action (e.g., `viewModel.onCountrySelect(country)`) inside `beforeEach`

### Then — Asserts the expected outcome
- Each `Then` is a leaf — it runs in its own isolated instance
- Assert on `testObserver.values().last()` or `.test().values()`

### And — Nests additional context under When
- Same rules as `When`; use for branching paths ("And the API returns an error")

## Assertion Patterns

```kotlin
// Last emitted state, exact equality (preferred when the state is a data class)
testObserver.values().last() shouldBeEqualTo UiState.Loaded(details)

// Type check (when payload doesn't matter)
testObserver.values().last() shouldBeInstanceOf UiState.Loading::class

// Error observable from LifecycleViewModel
viewModel.error.test().values().last().value shouldBeEqualTo expectedError
```

## Prohibited Patterns

- `mockk()`, `mock()`, `every { }`, `verify { }` — use the fake API + factories
- Setting mock API state inline (`api.getCountryListResult = ...`) — use a test factory
- `Thread.sleep()` — schedulers are trampolined; if a stream still needs time control, use RxJava `TestScheduler`
- Never add a test class to any Konsist baseline/allowed list; fix the test to follow the enforced pattern instead

## Steps

1. Create the test in the module matching the class under test (ViewModel tests in `viewmodels/src/test/kotlin/...`), named `<ClassName>BehaviorTest` or `<ClassName>Tests`.
2. Extend `BaseBehaviorSpec` for ViewModels; inject the subject with `by inject { parametersOf(...) }`.
3. Run it with the `run-unit-tests` skill and confirm it passes.
4. Run the `run-konsist` skill — test-related lint rules (e.g. `DoNotUseMockApisForSettingState`) must stay green.
