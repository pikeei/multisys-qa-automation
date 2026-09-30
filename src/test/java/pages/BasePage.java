package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.Config;

import java.time.Duration;
import java.util.List;

/**
 * Shared browser actions. Every action waits for the element first (no Thread.sleep).
 * Page classes extend this. Assertions are NOT here, they belong in the tests.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    private final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Config.WAIT_SECONDS));
    }

    protected void navigateTo(String path) {
        driver.get(Config.BASE_URL + path);
    }

    /** Waits for a condition. If it times out, the error says what we were waiting for. */
    protected <T> T waitFor(ExpectedCondition<T> condition, String description) {
        try {
            return wait.until(condition);
        } catch (TimeoutException e) {
            throw new TimeoutException("Timed out after " + Config.WAIT_SECONDS + "s waiting for " + description, e);
        }
    }

    protected WebElement waitForVisible(By locator) {
        return waitFor(ExpectedConditions.visibilityOfElementLocated(locator), "visibility of " + locator);
    }

    protected List<WebElement> findAll(By locator) {
        return waitFor(ExpectedConditions.presenceOfAllElementsLocatedBy(locator), "elements " + locator);
    }

    protected void click(By locator) {
        waitFor(ExpectedConditions.elementToBeClickable(locator), "clickable " + locator).click();
    }

    protected void type(By locator, String text) {
        WebElement field = waitForVisible(locator);
        field.clear();
        field.sendKeys(text);
    }

    protected String getText(By locator) {
        return waitForVisible(locator).getText().trim();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public void refresh() {
        driver.navigate().refresh();
    }
}
