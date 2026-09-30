package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CheckboxesPage;

public class CheckboxesTest extends BaseTest {

    @Test(description = "Scenario 3.1 - Modify checkbox state")
    public void toggleCheckboxes() {
        CheckboxesPage page = new CheckboxesPage(driver).open();

        
        Assert.assertTrue(page.getCount() >= 2, "Expected at least 2 checkboxes");

        // We don't assume the starting state: read it, click, check it flipped, click back, check it restored.
        for (int i = 0; i < page.getCount(); i++) {
            boolean original = page.isChecked(i);

            page.click(i);
            Assert.assertEquals(page.isChecked(i), !original, "Checkbox " + (i + 1) + " after 1st click");

            page.click(i);
            Assert.assertEquals(page.isChecked(i), original, "Checkbox " + (i + 1) + " after 2nd click");
        }
    }
}
