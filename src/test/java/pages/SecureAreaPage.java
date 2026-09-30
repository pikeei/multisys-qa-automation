package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SecureAreaPage extends BasePage {

    private static final By HEADER = By.cssSelector("h2");
    private static final By MESSAGE = By.id("flash");
    private static final By LOGOUT_BUTTON = By.cssSelector("a[href='/logout']");

    public SecureAreaPage(WebDriver driver) {
        super(driver);
    }

    public SecureAreaPage waitUntilLoaded() {
        waitFor(ExpectedConditions.urlContains("/secure"), "navigation to /secure");
        return this;
    }

    public String getHeaderText() {
        return getText(HEADER);
    }

    public String getMessage() {
        return getText(MESSAGE);
    }

    public LoginPage logout() {
        click(LOGOUT_BUTTON);
        waitFor(ExpectedConditions.urlContains("/login"), "navigation back to /login");
        return new LoginPage(driver);
    }
}
