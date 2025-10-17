# RestAssured Framework — Swagger Petstore API

End-to-end API test automation framework for the Swagger Petstore API built with Java and Rest Assured. The framework is designed to be simple to extend, easy to read, and CI-ready.

> Repo: `Ashu0729/RestAssuredFramework`  
> Description: “RestAssuredFramework of Petstore API”

## Tech stack

- Java (tests, helpers, models)
- Rest Assured for HTTP client and assertions
- JUnit/TestNG (choose one; example commands for both below)
- JSON (request/response payloads, test data)
- HTML/CSS/JavaScript (primarily from generated test reports and static assets)

---

## Getting started

### Prerequisites

- Java 8+ (Java 11+ recommended)
- Maven 3.8+
- Git

### Clone

```bash
git clone https://github.com/Ashu0729/RestAssuredFramework.git
cd RestAssuredFramework
```

### Configure

Set your target API base URL and environment. Common approaches:
- Java system properties: `-DbaseUrl=https://petstore.swagger.io/v2 -Denv=qa`
- `.properties` or `.yaml` file in `src/test/resources`
- Environment variables: `BASE_URL`, `ENV`, etc.

Example values for Swagger Petstore:
- Base URL (v2): `https://petstore.swagger.io/v2`
- Base URL (v3): `https://petstore3.swagger.io/api/v3`

> Pick one Petstore version and keep it consistent throughout your tests.

---

## Running tests

Choose your build tool and test runner.

### Maven + JUnit

```bash
mvn clean test -DbaseUrl=https://petstore.swagger.io/v2 -Denv=local
```

Run a single test class:
```bash
mvn -Dtest=PetTests test
```

Run a single test method:
```bash
mvn -Dtest=PetTests#shouldCreatePet test
```

### Maven + TestNG

```bash
mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/testng.xml -DbaseUrl=https://petstore.swagger.io/v2
```

## Project structure

A typical layout (adjust to match your repo):

```
.
├─ src
│  ├─ test
│  │  ├─ java                # Test classes, API clients, models, utilities
│  │  └─ resources           # testng.xml / junit-platform.properties, test data, schemas
│  └─ main
│     └─ java                # Optional: shared utils or domain models
├─ reports/                  # HTML reports (Extent/Allure/custom), if checked in
├─ target/ or build/         # Build outputs (usually gitignored)
├─ README.md
└─ pom.xml or build.gradle
```

If you use Allure or Extent:
- Allure results: `target/allure-results` (Maven) / `build/allure-results` (Gradle)
- Extent report: e.g., `reports/extent/ExtentReport.html`

---

## Configuration and environments

- Base URL: `baseUrl` system property or env var
- Authentication: add headers/tokens centrally (e.g., a Rest Assured `RequestSpecification`)
- Environments: dev/qa/stage/prod via profiles or property files, e.g.:
  - `src/test/resources/env/dev.properties`
  - `src/test/resources/env/qa.properties`

Central request specification example:

```java
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class Specs {
  public static RequestSpecification request() {
    String baseUrl = System.getProperty("baseUrl", "https://petstore.swagger.io/v2");
    return new RequestSpecBuilder()
      .setBaseUri(baseUrl)
      .addHeader("Accept", "application/json")
      .setContentType("application/json")
      .build();
  }
}
```

Usage:
```java
given().spec(Specs.request())...
```

---

## Test data

- Inline JSON bodies in tests for simple cases
- Externalize into `src/test/resources/data/*.json` or `.csv` for data-driven tests
- Use factories/builders for complex payloads

---

## Reports

Common report options:
- Surefire/Failsafe: `target/surefire-reports` (Maven)
- Gradle test report: `build/reports/tests/test/index.html`
- Extent: e.g., `reports/extent/ExtentReport.html`
- Allure: generate after tests:
  - Maven: `mvn allure:report` then open `target/site/allure-maven-plugin/index.html`
  - CLI: `allure serve target/allure-results`

Adjust paths to match your actual setup.

---

## CI/CD

You can run the test suite in CI with Java and your preferred build tool. Example GitHub Actions steps:

```yaml
name: API Tests
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '17'
      - name: Run tests (Maven)
        run: mvn -B clean test -DbaseUrl=https://petstore.swagger.io/v2
```

Switch to Gradle if that’s your build tool.

---

## Troubleshooting

- 404/405 responses: Ensure you’re using the correct Petstore version (v2 vs v3) and paths.
- Network issues: Some Petstore instances rate-limit or change availability; retry or switch the base URL.
- Failing schema validation: Update schema files to match the target API version.

---

## Contributing

- Create a new branch for your change.
- Add/Update tests for any new or changed behavior.
- Run the full test suite locally.
- Open a pull request with a clear description.

---

## License

Add a LICENSE file to clarify usage. If you’re unsure, consider MIT or Apache-2.0.

---

## References

- Swagger Petstore: https://petstore.swagger.io/
- Rest Assured: https://github.com/rest-assured/rest-assured
