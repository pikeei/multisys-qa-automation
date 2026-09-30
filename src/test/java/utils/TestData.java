package utils;

/** All test data in one place, so tests contain no hardcoded values. */
public final class TestData {

    // Login (public demo credentials shown on the login page)
    public static final String VALID_USERNAME = "tomsmith";
    public static final String VALID_PASSWORD = "SuperSecretPassword!";

    public static final String LOGIN_SUCCESS_MESSAGE = "You logged into a secure area!";
    public static final String LOGOUT_MESSAGE = "You logged out of the secure area!";
    public static final String INVALID_USERNAME_MESSAGE = "Your username is invalid!";
    public static final String INVALID_PASSWORD_MESSAGE = "Your password is invalid!";

    // New user for the POST test
    public static final String NEW_USER_NAME = "Juan Dela Cruz";
    public static final String NEW_USER_USERNAME = "jdelacruz";
    public static final String NEW_USER_EMAIL = "juan.delacruz@example.com";

    private TestData() {
    }

    /** Each row: description, username, password, expected error message. */
    public static Object[][] invalidLogins() {
        return new Object[][]{
                {"Wrong username", "wronguser", VALID_PASSWORD, INVALID_USERNAME_MESSAGE},
                {"Wrong password", VALID_USERNAME, "wrongpassword", INVALID_PASSWORD_MESSAGE},
                {"Wrong username and password", "wronguser", "wrongpassword", INVALID_USERNAME_MESSAGE}
        };
    }

    public static String newUserJson() {
        return String.format("{\"name\":\"%s\",\"username\":\"%s\",\"email\":\"%s\"}",
                NEW_USER_NAME, NEW_USER_USERNAME, NEW_USER_EMAIL);
    }
}
