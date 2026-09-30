package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class DynamicContentPage extends BasePage {

    private static final By TEXTS = By.cssSelector("#content .large-10.columns");
    private static final By AVATARS = By.cssSelector("#content .large-2.columns img");

    public DynamicContentPage(WebDriver driver) {
        super(driver);
    }

    public DynamicContentPage open() {
        navigateTo("/dynamic_content");
        findAll(TEXTS);
        return this;
    }

    /** The page text and image links as one list, so we can compare before and after a refresh. */
    public List<String> getContent() {
        List<String> content = new ArrayList<>();
        for (WebElement text : findAll(TEXTS)) {
            content.add(text.getText().trim());
        }
        for (WebElement avatar : driver.findElements(AVATARS)) {
            content.add(String.valueOf(avatar.getAttribute("src")));
        }
        return content;
    }
}
