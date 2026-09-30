package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckboxesPage extends BasePage {

    private static final By CHECKBOXES = By.cssSelector("#checkboxes input[type='checkbox']");

    public CheckboxesPage(WebDriver driver) {
        super(driver);
    }

    public CheckboxesPage open() {
        navigateTo("/checkboxes");
        findAll(CHECKBOXES);
        return this;
    }

    public int getCount() {
        return findAll(CHECKBOXES).size();
    }

    public boolean isChecked(int index) {
        return findAll(CHECKBOXES).get(index).isSelected();
    }

    public void click(int index) {
        findAll(CHECKBOXES).get(index).click();
    }
}
