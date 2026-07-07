#!/bin/bash
# Usage: bash .github/skills/run-unit-tests/run-tests.sh [--module <name>] [test-class-or-pattern ...]
#
# Examples:
#   bash .github/skills/run-unit-tests/run-tests.sh                                        # All unit tests
#   bash .github/skills/run-unit-tests/run-tests.sh CountryDetailsViewModelBehaviorTest    # Specific class
#   bash .github/skills/run-unit-tests/run-tests.sh "*CountryList*"                        # Pattern match
#   bash .github/skills/run-unit-tests/run-tests.sh --module logic                         # Specific module
#   bash .github/skills/run-unit-tests/run-tests.sh --module viewmodels ATest BTest        # Multiple classes
#
# Modules: viewmodels, logic, repositories (repo), interfaces (dto), networklogic,
#          feature, utils, app

set -uo pipefail

cd "$(git rev-parse --show-toplevel)"

MODULE=""
PATTERNS=()

while [[ $# -gt 0 ]]; do
    case "$1" in
        --module)
            if [ $# -lt 2 ]; then
                echo "Error: --module requires a module name."
                echo "Available modules: viewmodels, logic, repositories, interfaces, networklogic, feature, utils, app"
                exit 1
            fi
            MODULE="$2"
            shift 2
            ;;
        *)
            PATTERNS+=("$1")
            shift
            ;;
    esac
done

# Android modules test with testDebugUnitTest; plain Kotlin modules with test.
resolve_module() {
    case "$1" in
        viewmodel*)   GRADLE_MODULE="viewmodels"; TEST_TASK="testDebugUnitTest" ;;
        logic)        GRADLE_MODULE="logic"; TEST_TASK="test" ;;
        repo*)        GRADLE_MODULE="repositories"; TEST_TASK="test" ;;
        interface*|dto) GRADLE_MODULE="interfaces"; TEST_TASK="test" ;;
        networklogic|network) GRADLE_MODULE="networklogic"; TEST_TASK="test" ;;
        feature*)     GRADLE_MODULE="feature"; TEST_TASK="testDebugUnitTest" ;;
        utils)        GRADLE_MODULE="utils"; TEST_TASK="test" ;;
        app)          GRADLE_MODULE="app"; TEST_TASK="testDebugUnitTest" ;;
        *)            echo "Unknown module: $1"; exit 1 ;;
    esac
}

ALL_MODULE_TASKS=(
    "viewmodels:testDebugUnitTest"
    "logic:test"
    "repositories:test"
    "interfaces:test"
    "networklogic:test"
    "feature:testDebugUnitTest"
    "utils:test"
    "app:testDebugUnitTest"
)

GRADLE_ARGS=()

if [ -n "$MODULE" ]; then
    resolve_module "$MODULE"
    GRADLE_ARGS+=("${GRADLE_MODULE}:${TEST_TASK}")
    if [ ${#PATTERNS[@]} -gt 0 ]; then
        for p in "${PATTERNS[@]}"; do
            GRADLE_ARGS+=("--tests" "*${p}*")
        done
        echo "Running tests in ${GRADLE_MODULE} matching: ${PATTERNS[*]}"
    else
        echo "Running all tests in ${GRADLE_MODULE}..."
    fi
elif [ ${#PATTERNS[@]} -eq 0 ]; then
    GRADLE_ARGS+=("${ALL_MODULE_TASKS[@]}")
    echo "Running ALL unit tests (excluding Konsist)..."
else
    # Auto-detect module from the first pattern
    SEARCH_NAME=$(echo "${PATTERNS[0]}" | tr -d '*')
    ALL_MATCHES=$(find . \( -path "*/src/test/java/*" -o -path "*/src/test/kotlin/*" \) -name "*${SEARCH_NAME}*.kt" 2>/dev/null | grep -v testFixtures | grep -v "/konsist/" | grep -v "/build/" | grep -v "/.claude/")

    if [ -z "$ALL_MATCHES" ]; then
        echo "Error: No test file matching '${SEARCH_NAME}' found."
        exit 1
    fi

    MODULES_FOUND=$(echo "$ALL_MATCHES" | sed -E 's|^\./([^/]+)/.*|\1|' | sort -u)
    MODULE_COUNT=$(echo "$MODULES_FOUND" | wc -l | tr -d ' ')

    if [ "$MODULE_COUNT" -gt 1 ]; then
        echo "Pattern matches tests in multiple modules:"
        echo "$MODULES_FOUND"
        echo "Re-run with --module <name> to disambiguate."
        exit 1
    fi

    resolve_module "$MODULES_FOUND"
    GRADLE_ARGS+=("${GRADLE_MODULE}:${TEST_TASK}")
    for p in "${PATTERNS[@]}"; do
        GRADLE_ARGS+=("--tests" "*${p}*")
    done
    echo "Running tests in ${GRADLE_MODULE} matching: ${PATTERNS[*]}"
fi

TMPFILE=$(mktemp)
TIMEFILE=$(mktemp)
trap 'rm -f "$TMPFILE" "$TIMEFILE"' EXIT INT TERM
./gradlew --console=plain "${GRADLE_ARGS[@]}" > "$TMPFILE" 2>&1
EXIT_CODE=$?

if [ $EXIT_CODE -eq 0 ]; then
    echo ""
    grep "BUILD SUCCESSFUL" "$TMPFILE"
    echo "TESTS PASSED"
else
    echo ""
    echo "TESTS FAILED"
    echo ""

    FAILED_TESTS=$(grep "FAILED$" "$TMPFILE" | grep -v "^> Task" | grep -v "^$")
    if [ -n "$FAILED_TESTS" ]; then
        echo "Failed tests:"
        echo "$FAILED_TESTS"
        echo ""
    fi

    grep "tests completed" "$TMPFILE" || true

    # Extract failure details from JUnit XML, keeping only the text before the stack trace.
    XML_FILES=$(find . -path "*/build/test-results/*/TEST-*.xml" -newer "$TIMEFILE" -not -path "*/.claude/*" -exec grep -lE '<(failure|error)[ >]' {} \; 2>/dev/null)
    DETAILS=""
    if [ -n "$XML_FILES" ]; then
        DETAILS=$(echo "$XML_FILES" | python3 -c "
import sys, re, xml.etree.ElementTree as ET

stack = re.compile(r'^\s*(at\s|Caused by:|\.\.\.\s*\d+\s+more\b)')
out = []
for path in (l.strip() for l in sys.stdin if l.strip()):
    try:
        tree = ET.parse(path)
    except Exception:
        continue
    for tc in tree.findall('.//testcase'):
        for err in tc.findall('failure') + tc.findall('error'):
            lines = []
            for line in (err.text or '').splitlines():
                if stack.match(line):
                    break
                lines.append(line.rstrip())
            body = '\n'.join(lines).strip() or (err.get('message') or err.get('type') or '').strip()
            if body:
                cls = (tc.get('classname', '') or '').rsplit('.', 1)[-1]
                out.append(f'• {cls} > {tc.get(\"name\", \"\")}\n{body}')
print('\n\n'.join(out))
")
    fi

    if [ -n "$DETAILS" ]; then
        echo ""
        echo "Failure details:"
        echo ""
        echo "$DETAILS"
    else
        echo ""
        sed -n '/^\* What went wrong:/,/^\* Try:/{ /^\* Try:/d; p; }' "$TMPFILE"
    fi
fi

rm -f "$TMPFILE" "$TIMEFILE"
exit $EXIT_CODE
