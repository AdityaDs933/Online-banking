Setup & Dependencies

- This project is a plain Java/Servlet application. It currently expects jars to live in `lib/` for running and testing without a build tool.
- Required libraries (place their jars into `lib/`):
  - jbcrypt (org.mindrot.jbcrypt) for password hashing (jbcrypt-0.4.jar)
  - JUnit 5 jars for running tests (see `lib/README.txt`)

Recommended: migrate to Maven or Gradle and declare dependencies there (highly recommended).

Running locally (quick)

1. Deploy the project to a servlet container (Tomcat/Jetty) and configure a DataSource or ensure the database schema matches the tables described above.
2. Place required jars into `lib/` if not using a build tool.
3. Start the servlet container and open `index.html` or `login.html`.

Testing & CI

- Basic JUnit 5 test skeletons are provided in `src/test/java`. A GitHub Actions workflow (`.github/workflows/java-ci.yml`) is included to run tests. The workflow assumes test jars are available in `lib/` or that you switch to a build tool.

Security Notes

- Passwords are now hashed using BCrypt; ensure `lib/jbcrypt-0.4.jar` is available or migrate to a dependency manager.
- Database queries use `PreparedStatement` to reduce SQL injection risk — continue this pattern everywhere.
- Recommendations: enable HTTPS, add session timeouts, use CSRF tokens, and sanitize/escape user-supplied output in JSPs.

Next Improvements

- Add unit tests (AccountService, TransactionService, InputValidator) using JUnit/Mockito.
- Replace custom connection pooling with a production-ready pool (HikariCP) or container-managed DataSource.
- Add CONTRIBUTING.md and issue templates to encourage collaboration.
