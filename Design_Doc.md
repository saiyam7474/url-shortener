# Engineering Notes

## 1. How I used AI

I used AI mainly for brainstorming, reviewing the structure, and getting help
with implementation details when I was stuck. I worked through the project
incrementally instead of generating the complete solution at once.

I made the main decisions around the project structure, database choice,
short-code generation, duplicate URL behavior, and error handling.

## 2. Changes I made during development

One issue I found while testing was that invalid URLs were initially returning
a 500 response. I changed the error handling so invalid input returns 400.

I also ran into a problem with the file-based H2 database when running the
application and tests at the same time. The database file was locked, so I
separated the test database and used an in-memory database for tests.

For short-code generation, I considered using a database sequence but decided
to keep the implementation simpler with random Base62 codes and an existence
check before saving.

## 3. Main trade-offs

I chose H2 instead of PostgreSQL because it keeps the project easy to run
locally and is enough for the scope of this exercise.

For short codes, I used random Base62 strings. A counter-based approach would
give deterministic IDs, but it would add more database/coordination logic than
I felt was necessary here. I handle collisions by checking whether the code
already exists before saving it.

For duplicate URLs, I decided to allow the same URL to be shortened multiple
times. Each request can therefore get a different short code. Custom aliases
are treated separately and must be unique.

## 4. What I would improve with more time

I would move to PostgreSQL for a production setup, add stronger validation for
custom aliases, and add more tests around concurrency and database failures.

I would also add better logging and monitoring and revisit the short-code
generation approach if the service needed to operate at much larger scale.