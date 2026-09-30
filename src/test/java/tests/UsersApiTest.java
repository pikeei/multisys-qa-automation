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

/**
 * API tests. Note: JSONPlaceholder is a fake API. A POST returns 201 and a new id,
 * but the data is not saved, so we do not GET the new user afterwards.
 */
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
        return new Object[][]{{1}, {5}, {10}};
    }

    @Test(dataProvider = "userIds", description = "Scenario 4.2 - GET single user")
    public void getSingleUser(int id) {
        request().when().get(USERS + "/" + id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("name", notNullValue())
                .body("username", notNullValue())
                .body("email", containsString("@"))
                .body("address.city", notNullValue())
                .body("company.name", notNullValue());
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
