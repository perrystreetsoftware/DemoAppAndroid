#!/bin/bash
# Usage: bash .github/skills/run-konsist/run-konsist.sh [rule-name ...]
#
# Examples:
#   bash .github/skills/run-konsist/run-konsist.sh                                    # Run ALL rules
#   bash .github/skills/run-konsist/run-konsist.sh DoesNotHaveInitializer             # One rule
#   bash .github/skills/run-konsist/run-konsist.sh DoesNotHaveInitializer StartsWithI # Multiple rules
#
# The rule name matches the Kotlin class name in konsist/src/test/.

set -uo pipefail

cd "$(git rev-parse --show-toplevel)"

# Build the gradle command as an array (avoids eval/injection)
GRADLE_ARGS=("konsist:test")
if [ $# -eq 0 ]; then
    echo "Running ALL Konsist lint rules..."
elif [ $# -eq 1 ]; then
    GRADLE_ARGS+=("--tests" "*${1}*")
    echo "Running Konsist rule: $1"
else
    for rule in "$@"; do
        GRADLE_ARGS+=("--tests" "*${rule}*")
    done
    echo "Running Konsist rules: $*"
fi

# Capture full output
TMPFILE=$(mktemp)
TIMEFILE=$(mktemp)
trap 'rm -f "$TMPFILE" "$TIMEFILE"' EXIT INT TERM
./gradlew --console=plain "${GRADLE_ARGS[@]}" > "$TMPFILE" 2>&1
EXIT_CODE=$?

if [ $EXIT_CODE -eq 0 ]; then
    echo ""
    grep "BUILD SUCCESSFUL" "$TMPFILE"
    echo "KONSIST LINT RULES PASSED"
else
    echo ""
    echo "KONSIST LINT RULES FAILED"
    echo ""

    # Print failed rule names
    FAILED_RULES=$(grep "FAILED$" "$TMPFILE" | grep -v "^> Task" | grep -v "^$")
    if [ -n "$FAILED_RULES" ]; then
        echo "Failed rules:"
        echo "$FAILED_RULES"
        echo ""
    fi

    # Print test summary line
    grep "tests completed" "$TMPFILE" || true

    # Extract violation details (rule message + offending declarations) from the
    # JUnit XML, keeping only the text before the stack trace.
    XML_FILES=$(find ./konsist -path "*/build/test-results/*/TEST-*.xml" -newer "$TIMEFILE" -exec grep -lE '<(failure|error)[ >]' {} \; 2>/dev/null)
    VIOLATIONS=""
    if [ -n "$XML_FILES" ]; then
        VIOLATIONS=$(echo "$XML_FILES" | python3 -c "
import sys, re, xml.etree.ElementTree as ET

# A line that starts the stack trace — everything from here on is noise.
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
                rule = (tc.get('classname', '') or '').rsplit('.', 1)[-1]
                out.append(f'• {rule}\n{body}')
print('\n\n'.join(out))
")
    fi

    if [ -n "$VIOLATIONS" ]; then
        echo ""
        echo "Violation details:"
        echo ""
        echo "$VIOLATIONS"
    else
        # No structured violations (e.g. a compile/config error) — show the cause.
        echo ""
        sed -n '/^\* What went wrong:/,/^\* Try:/{ /^\* Try:/d; p; }' "$TMPFILE"
    fi
fi

rm -f "$TMPFILE" "$TIMEFILE"
exit $EXIT_CODE
