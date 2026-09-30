package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.Config;
import utils.TestData;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;


public class UsersApiTest {

    private static final String USERS = "/users";

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    private RequestSpecification request() {
        return given()
                .baseUri(Config.API_URL)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("User-Agent", "Mozilla/5.0 (QA-Automation-Exam)");
    }

    @Test(description = "Scenario 4.1 - GET all users")
    public void getAllUsers() {
        request().when().get(USERS)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", greaterThan(0))
                .body(matchesJsonSchemaInClasspath("schemas/users-schema.json"));
    }

    @DataProvider(name = "userIds")
    public Object[][] userIds() {
        return new Object[][]{
                {1, "Leanne Graham", "Bret", "Sincere@april.biz", "Gwenborough", "Romaguera-Crona"},
                {2, "Ervin Howell", "Antonette", "Shanna@melissa.tv", "Wisokyburgh", "Deckow-Crist"}
        };
    }

    @Test(dataProvider = "userIds", description = "Scenario 4.2 - GET single user")
    public void getSingleUser(int id, String name, String username, String email, String city, String companyName) {
        request().when().get(USERS + "/" + id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("name", equalTo(name))
                .body("username", equalTo(username))
                .body("email", equalTo(email))
                .body("address.city", equalTo(city))
                .body("company.name", equalTo(companyName));
    }

    @Test(description = "Scenario 4.3 - POST creates a user")
    public void createUser() {
        List<Integer> existingIds = request().when().get(USERS)
                .then().statusCode(200)
                .extract().jsonPath().getList("id", Integer.class);

        Response response = request().body(TestData.newUserJson())
                .when().post(USERS)
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .extract().response();

        int newId = response.jsonPath().getInt("id");
        Assert.assertFalse(existingIds.contains(newId), "New id " + newId + " must be unique");
        Assert.assertEquals(response.jsonPath().getString("name"), TestData.NEW_USER_NAME, "name");
        Assert.assertEquals(response.jsonPath().getString("username"), TestData.NEW_USER_USERNAME, "username");
        Assert.assertEquals(response.jsonPath().getString("email"), TestData.NEW_USER_EMAIL, "email");
    }

    @Test(description = "Bonus - GET unknown user returns 404")
    public void getUnknownUser() {
        request().when().get(USERS + "/9999")
                .then()
                .statusCode(404);
    }
}
