# Contributing

Thanks for your interest in contributing.

## Development workflow

1. Create a feature branch.
2. Keep changes focused and avoid unrelated formatting churn.
3. Add/adjust tests for the behavior you change.
4. Open a pull request against `main`.

## Prerequisites

- JDK 17
- Maven 3.9+ (or rely on GitHub Actions CI)

## Tests

From the `Online-banking/` directory:

- `mvn -q test`
- `mvn -q verify`

## Database and migrations

Schema changes must be done via Flyway migrations under `src/main/resources/db/migration`.
Avoid editing the schema directly in code or ad-hoc SQL scripts.

## Security expectations

- Use `PreparedStatement` for SQL (no string concatenation with user input).
- For any JSP/HTML form that submits `POST`, include the hidden `_csrf` field.
- Do not commit secrets. Use env vars (`JDBC_URL`, `JDBC_USER`, `JDBC_PASSWORD`, `JDBC_DRIVER`).
