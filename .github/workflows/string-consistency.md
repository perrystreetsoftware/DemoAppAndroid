# String Consistency Guidelines

This file defines the copy guidelines for SCRUFF and Jack'd apps at Perry Street Software.
The agent should use these rules as the source of truth when reviewing English strings in pull requests.

## Overview

Copy is written originally in English and translated into 13 languages:
Spanish, French, German, Arabic, Portuguese, Korean, Japanese, Chinese (Traditional), and Chinese (Simplified).

All strings must be written with translation in mind.

## Style Reference

We align our copy with the [Apple Style Guide](https://support.apple.com/guide/applestyleguide/welcome/web).

## Core Principles

### Neutral and non-judgmental
- Use neutral, fact-based language. Avoid emotional or opinionated phrasing.
- Do not shame or blame the user in error states, rejections, or negative outcomes.
- Avoid language that could feel accusatory (e.g. "You did something wrong") — prefer passive or system-focused phrasing (e.g. "Something went wrong").

### Avoid first-person pronouns
- Do not use "we", "us", or "our" — following Apple Style Guide recommendations.
- Good: "An error occurred." — Bad: "We encountered an error."

### No idioms
- Avoid idioms, colloquialisms, or culture-specific expressions that do not translate well.
- Bad: "Hang tight", "Bear with us", "You're all set"
- Good: "Please wait", "Please try again", "Done"

### No English contractions
- Write out full words instead of contractions to make translation easier and keep a neutral tone.
- Bad: "You don't have permission", "We can't load this"
- Good: "You do not have permission", "This could not be loaded"

### Easy to translate
- Keep sentences short and direct.
- Avoid ambiguous terms that could have multiple meanings in other languages.
- Prefer concrete, specific words over abstract ones.

## Tone for Negative States

When something goes wrong (errors, rejections, unavailable content):
- Be factual and calm — do not dramatize.
- Do not apologize excessively or use filler phrases like "Oops!" or "Uh oh!".
- Focus on what happened and what the user can do next, if applicable.
- Good: "This content is not available." — Bad: "Oops! We couldn't load this for you."

## Formatting Conventions

- **Titles / headings**: Title case (e.g. "Save as Favorite", "Connection Error")
- **Button labels**: Title case, no trailing punctuation (e.g. "Try Again", "Cancel")
- **Body messages / descriptions**: Sentence case, with a trailing period (e.g. "An error occurred. Please try again later.")
- **Error structure**: Use a title + message pair when space allows. Title is short and descriptive; message provides detail or next steps.
