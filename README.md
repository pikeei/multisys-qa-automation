# Multisys QA Examination -  Automated Test Scripts

UI tests for `http://the-internet.herokuapp.com` and API tests for
`https://jsonplaceholder.typicode.com/users`.

## Project structure

```
multisys-qa-automation/
├── pom.xml          Maven: the libraries used and how to run the tests
├── testng.xml       Which test classes to run and the report listener
└── src/test/
    ├── java/
    │   ├── pages/   One class per web page (locators + actions)
    │   │     BasePage, LoginPage, SecureAreaPage, DynamicContentPage, CheckboxesPage
    │   ├── tests/   The test cases (assertions live here)
    │   │     BaseTest, LoginTest, DynamicContentTest, CheckboxesTest, UsersApiTest
    │   └── utils/   Config (settings), TestData (test values), TestListener (HTML report)
    └── resources/
        └── schemas/users-schema.json   Expected shape of the users API response
```

## Framework structure and design

| Element | Description |
|---|---|
| **Framework Used** | Selenium WebDriver 4 with Java and TestNG for UI tests; REST Assured for API tests; Maven build |
| **Design Pattern** | Page Object Model: each page is a class in `pages/` holding its locators and actions; tests in `tests/` only call page methods and assert results |
| **Test Data Handling** | All credentials, expected messages and API payload values are in `utils/TestData.java`; URLs, browser and timeouts are in `utils/Config.java` |
| **Reporting Tool** | ExtentReports HTML report with a screenshot on UI test failure |

## Prerequisites

JDK 17 or newer, Maven 3.9+, Google Chrome, and internet access.

## How to run

```bash
mvn clean test                      # run everything
mvn clean test -Dheadless=true      # no visible browser window
mvn clean test -Dbrowser=firefox    # use Firefox instead of Chrome
mvn -Dtest=tests.CheckboxesTest test #Running individual runs
```

Open the report at `target/extent-report/index.html`.

## Scenarios covered

| Scenario | Class | Tests |
|---|---|---|
| 1. Login | `LoginTest` | Successful login; invalid username/password (3 cases); empty credentials; bonus: logout |
| 2. Dynamic content | `DynamicContentTest` | Content changes after refresh |
| 3. Checkboxes | `CheckboxesTest` | Click each checkbox, verify the state flips, then restore it |
| 4. API | `UsersApiTest` | GET all (200 + schema); GET single for ids 1, 5, 10; POST (201 + new unique id); bonus: GET unknown id returns 404 |

## Notes

- No `Thread.sleep`: every action waits for its element, and timeouts say what was being waited for.
- Dynamic content is random and can repeat, so the test refreshes up to 5 times until it changes.
- Checkbox test does not assume the starting state.
- JSONPlaceholder is a fake API: POST returns 201 and a new id but does not save the data.
- Each UI test opens a fresh browser and always closes it, even when the test fails.
