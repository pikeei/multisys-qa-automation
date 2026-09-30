package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DynamicContentPage;
import utils.Config;

import java.util.List;

public class DynamicContentTest extends BaseTest {

    @Test(description = "Scenario 2.1 - Content changes on refresh")
    public void contentChangesOnRefresh() {
        DynamicContentPage page = new DynamicContentPage(driver).open();
        List<String> before = page.getContent();
        Assert.assertFalse(before.isEmpty(), "Page should show content");

        // The content is random, so it can occasionally repeat. Refresh a few times until it changes.
        boolean changed = false;
        for (int i = 0; i < Config.MAX_REFRESH_ATTEMPTS && !changed; i++) {
            page.refresh();
            changed = !page.getContent().equals(before);
        }

        Assert.assertTrue(changed, "Content did not change after " + Config.MAX_REFRESH_ATTEMPTS + " refreshes");
    }
}
