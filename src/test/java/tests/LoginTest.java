package tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.SecureAreaPage;
import utils.TestData;

public class LoginTest extends BaseTest {

    @DataProvider(name = "invalidLogins")
    public Object[][] invalidLogins() {
        return TestData.invalidLogins();
    }

    @Test(priority = 1, description = "Scenario 1.1 - Successful login")
    public void successfulLogin() {
        SecureAreaPage secure = new LoginPage(driver).open()
                .loginAs(TestData.VALID_USERNAME, TestData.VALID_PASSWORD);

        Assert.assertTrue(secure.getCurrentUrl().endsWith("/secure"), "Should be on /secure");
        Assert.assertEquals(secure.getHeaderText(), "Secure Area", "Page header");
        Assert.assertTrue(secure.getMessage().contains(TestData.LOGIN_SUCCESS_MESSAGE),
                "Unexpected message: " + secure.getMessage());
    }

    @Test(priority = 2, dataProvider = "invalidLogins", description = "Scenario 1.2 - Invalid username/password")
    public void invalidLogin(String description, String username, String password, String expectedMessage) {
        LoginPage login = new LoginPage(driver).open().loginExpectingFailure(username, password);

        Assert.assertTrue(login.getMessage().contains(expectedMessage),
                description + " - unexpected message: " + login.getMessage());
        Assert.assertTrue(login.getCurrentUrl().contains("/login"), "Should stay on the login page");
    }

    @Test(priority = 3, description = "Scenario 1.3 - Empty credentials")
    public void emptyCredentials() {
        LoginPage login = new LoginPage(driver).open().loginExpectingFailure("", "");

        Assert.assertTrue(login.getMessage().contains(TestData.INVALID_USERNAME_MESSAGE),
                "Unexpected message: " + login.getMessage());
        Assert.assertTrue(login.getCurrentUrl().contains("/login"), "Should stay on the login page");
    }

    @Test(priority = 4, description = "Bonus - Logout")
    public void logout() {
        LoginPage login = new LoginPage(driver).open()
                .loginAs(TestData.VALID_USERNAME, TestData.VALID_PASSWORD)
                .logout();

        Assert.assertTrue(login.getMessage().contains(TestData.LOGOUT_MESSAGE),
                "Unexpected message: " + login.getMessage());
    }
}
