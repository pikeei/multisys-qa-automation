package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private static final By USERNAME = By.id("username");
    private static final By PASSWORD = By.id("password");
    private static final By LOGIN_BUTTON = By.cssSelector("button[type='submit']");
    private static final By MESSAGE = By.id("flash");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        navigateTo("/login");
        waitForVisible(USERNAME);
        return this;
    }

    /** Logs in and expects to reach the secure area. */
    public SecureAreaPage loginAs(String username, String password) {
        submit(username, password);
        return new SecureAreaPage(driver).waitUntilLoaded();
    }

    /** Tries to log in and stays on this page (for negative tests). */
    public LoginPage loginExpectingFailure(String username, String password) {
        submit(username, password);
        return this;
    }

    public String getMessage() {
        return getText(MESSAGE);
    }

    private void submit(String username, String password) {
        type(USERNAME, username);
        type(PASSWORD, password);
        click(LOGIN_BUTTON);
    }
}
