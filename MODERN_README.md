# Online Banking — Modernized Notes

Quick start (development):

- Ensure Java 17 and Maven are installed.
- Set environment variables for DB connection (recommended):

```bash
export JDBC_URL=jdbc:h2:mem:bank;DB_CLOSE_DELAY=-1
export JDBC_USER=sa
export JDBC_PASSWORD=
```

- Build and run tests locally:

```bash
mvn clean test
```

- Package the WAR to deploy into a servlet container:

```bash
mvn package
# then deploy target/online-banking.war to Tomcat or similar
```

Notes:
- CSRF protection filter is added; forms must include a hidden `_csrf` field. Legacy login endpoint is temporarily exempt.
- DB configuration is read from `JDBC_URL`, `JDBC_USER`, `JDBC_PASSWORD`, `JDBC_DRIVER` environment variables.
- Code quality tools (SpotBugs, Checkstyle, JaCoCo) are configured in `pom.xml` and run in the `verify` phase.

Next recommended tasks:
- Finish `Vector` -> `List` migration and remove remaining raw types.
- Migrate legacy HTML forms to JSPs to include CSRF tokens.
- Replace System.out/err with SLF4J logging across the codebase.
- Add Flyway migrations under `src/main/resources/db/migration` and run them via Maven in CI.
